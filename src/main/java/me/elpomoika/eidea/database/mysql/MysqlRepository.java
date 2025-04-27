package me.elpomoika.eidea.database.mysql;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.models.Idea;
import me.elpomoika.eidea.util.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MysqlRepository implements Repository {
    private final MysqlService service;
    private final CooldownManager manager;
    private final EIdea plugin;

    public MysqlRepository(MysqlService service, CooldownManager manager, EIdea plugin) {
        this.service = service;
        this.manager = manager;
        this.plugin = plugin;
    }

    @Override
    public void addPlayer(Player player, String idea) {
        int requestCount = getRequestCountByPlayer(player);
        if (idea.isEmpty()) return;
        if (isIdeaAlreadyExists(idea)) {
            player.sendMessage(ChatColor.RED + "Идея не уникальна!");
            return;
        }

        int maxRequest = 1;

        if (requestCount >= maxRequest) {
            player.sendMessage("Вы не можете больше отправлять заявок.");
            return;
        }

        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("INSERT INTO players (uuid, idea, status) VALUES (?, ?, 'В ОЖИДАНИИ')")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, idea);
            preparedStatement.executeUpdate();
            player.sendMessage("Ваша заявка успешно отправлена!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPlayerPremium(Player player, String idea) {
        int requestCount = getRequestCountByPlayer(player);
        if (idea.isEmpty()) return;
        if (isIdeaAlreadyExists(idea)) {
            player.sendMessage(ChatColor.RED + "Идея не уникальна!");
            return;
        }

        boolean hasPermission = player.hasPermission("eidea.premium");
        int maxRequests = hasPermission ? 3 : 1;

        if (requestCount >= maxRequests) {
            player.sendMessage("Вы не можете отправить больше заявок.");
            return;
        }

        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("INSERT INTO players (uuid, idea, status) VALUES (?, ?, 'В ОЖИДАНИИ')")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, idea);
            preparedStatement.executeUpdate();
            player.sendMessage("Ваша заявка успешно отправлена!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPlayer(int id) {
        String playerUUIDString = null;
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("SELECT uuid FROM players WHERE id = ?")){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                playerUUIDString = resultSet.getString("uuid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        UUID playerUUID = UUID.fromString(playerUUIDString);

        return Bukkit.getOfflinePlayer(playerUUID).getName();
    }

    @Override
    public String getStatus(int id) {
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("SELECT status FROM players WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("status");
            }
            return "Неизвестно";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus(int id, String status) {
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("UPDATE players SET status = ? WHERE id = ?")) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            if (status.equalsIgnoreCase("ОТКЛОНЕНО")) {
                Player player = Bukkit.getPlayerExact(getPlayer(id));
                if (player == null) return;

                manager.setCooldown(player.getUniqueId(), Duration.ofSeconds(plugin.getConfig().getLong("cooldown")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Idea> getAllIdeas() {
        List<Idea> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("SELECT id, uuid, idea, status FROM players")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Idea(
                        resultSet.getInt("id"),
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("idea"),
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public int getRequestCountByPlayer(Player player) {
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("SELECT COUNT(*) FROM players WHERE uuid = ? AND status = 'В ОЖИДАНИИ'")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isIdeaAlreadyExists(String idea) {
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("SELECT COUNT(*) FROM players WHERE idea = ?")){

            preparedStatement.setString(1, idea);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

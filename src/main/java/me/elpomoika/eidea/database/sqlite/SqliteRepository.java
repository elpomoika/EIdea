package me.elpomoika.eidea.database.sqlite;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.models.FeedbackType;
import me.elpomoika.eidea.models.Feedback;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.util.cooldown.CooldownManager;
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

public class SqliteRepository implements Repository {
    private final SqliteService service;
    private final FeedbackMaster plugin;

    public SqliteRepository(SqliteService service, FeedbackMaster plugin) {
        this.service = service;
        this.plugin = plugin;
    }

    @Override
    public void addPlayer(Player player, String feedback, FeedbackType type) {
        int requestCount = getRequestCountByPlayer(player, FeedbackType.fromId(type.getId()));
        if (feedback.isEmpty()) return;

        if (isIdeaAlreadyExists(feedback)) {
            sendConfigMessage(player, type, "isn't-unique");
            return;
        }

        int maxRequest = 1;

        if (requestCount >= maxRequest) {
            sendConfigMessage(player, type, "limit-message");
            return;
        }

        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("INSERT INTO players (uuid, idea, type, status) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, feedback);
            preparedStatement.setInt(3, type.getId());
            preparedStatement.setByte(4, Status.PENDING.getId());

            preparedStatement.executeUpdate();
            sendConfigMessage(player, type, "successfully-send");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addPlayerPremium(Player player, String feedback, FeedbackType type) {
        int requestCount = getRequestCountByPlayer(player, FeedbackType.fromId(type.getId()));
        if (feedback.isEmpty()) return;

        if (isIdeaAlreadyExists(feedback)) {

            sendConfigMessage(player, type, "isn't-unique");
            return;
        }

        boolean hasPermission = player.hasPermission("feedback.premium");
        int maxRequests = hasPermission ? 3 : 1;

        if (requestCount >= maxRequests) {
            sendConfigMessage(player, type, "limit-message");
            return;
        }

        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("INSERT INTO players (uuid, idea, type, status) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, feedback);
            preparedStatement.setInt(3, type.getId());
            preparedStatement.setByte(4, Status.PENDING.getId());

            preparedStatement.executeUpdate();
            sendConfigMessage(player, type, "successfully-send");
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
    public void updateStatus(int id, byte status, CooldownManager manager) {
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("UPDATE players SET status = ? WHERE id = ?")) {
            preparedStatement.setByte(1, status);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            Status ideaStatus = Status.fromId(status);
            if (ideaStatus == Status.DECLINED) {
                Player player = Bukkit.getPlayerExact(getPlayer(id));
                if (player == null) return;

                manager.setCooldown(player.getUniqueId(), Duration.ofSeconds(plugin.getConfig().getLong("cooldown")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Feedback> getAllFeedback(FeedbackType filterType) {
        List<Feedback> list = new ArrayList<>();

        final String sql = "SELECT id, uuid, idea, type, status FROM players" +
                (filterType != null ? " WHERE type = ?" : "");

        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement(sql)) {
            if (filterType != null) {
                preparedStatement.setInt(1, filterType.getId());
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Feedback(
                        resultSet.getInt("id"),
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("idea"),
                        Status.fromId(resultSet.getByte("status")),
                        FeedbackType.fromId(resultSet.getInt("type"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public int getRequestCountByPlayer(Player player, FeedbackType type) {
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("SELECT COUNT(*) FROM players WHERE uuid = ? AND status = ? AND type = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setByte(2, Status.PENDING.getId());
            preparedStatement.setInt(3, type.getId());
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

    private void sendConfigMessage(Player player, FeedbackType type, String messageKey) {
        String path = "message." + type.name().toLowerCase() + "." + type.name().toLowerCase() + "-" + messageKey;
        String message = plugin.getConfig().getString(path);
        if (message != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            player.sendMessage(ChatColor.RED + "Error: Missing message configuration for " + path);
            plugin.getLogger().warning("Missing message configuration: " + path);
        }
    }
}

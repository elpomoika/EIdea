package me.elpomoika.eidea.database.mysql;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.AbstractRepository;
import me.elpomoika.eidea.models.FeedbackType;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.util.cooldown.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.UUID;

public class MysqlRepository extends AbstractRepository {

    public MysqlRepository(MysqlService service, FeedbackMaster plugin) {
        super(service, plugin);
    }

    @Override
    public void addPlayer(Player player, String feedback, FeedbackType type) {
        int requestCount = getRequestCountByPlayer(player, FeedbackType.fromId(type.getId()));
        if (feedback.isEmpty()) return;

        if (isFeedbackAlreadyExists(feedback)) {
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
        if (isFeedbackAlreadyExists(feedback)) {
            sendConfigMessage(player, type, "isn't-unique");
            return;
        }

        // поменять perms
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
}

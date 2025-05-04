package me.elpomoika.eidea.database;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.models.Feedback;
import me.elpomoika.eidea.models.FeedbackType;
import me.elpomoika.eidea.models.Status;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractRepository implements Repository {

    protected final DatabaseConnection service;
    protected final FeedbackMaster plugin;

    protected AbstractRepository(DatabaseConnection service, FeedbackMaster plugin) {
        this.service = service;
        this.plugin = plugin;
    }

    @Override
    public List<Feedback> getAllFeedback(FeedbackType filterType) {
        final List<Feedback> list = new ArrayList<>();

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

    @Override
    public boolean isFeedbackAlreadyExists(String feedback) {
        try (PreparedStatement preparedStatement = service.getConnection().prepareStatement("SELECT COUNT(*) FROM players WHERE idea = ?")){

            preparedStatement.setString(1, feedback);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendConfigMessage(Player player, FeedbackType type, String messageKey) {
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

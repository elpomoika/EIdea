package me.elpomoika.eidea.database;

import me.elpomoika.eidea.models.Feedback;
import me.elpomoika.eidea.models.FeedbackType;
import me.elpomoika.eidea.util.cooldown.CooldownManager;
import org.bukkit.entity.Player;

import java.util.List;

public interface Repository {
    void addPlayer(Player player, String feedback, FeedbackType type);

    void addPlayerPremium(Player player, String feedback, FeedbackType type);

    String getPlayer(int id);
    void updateStatus(int id, byte status, CooldownManager manager);
    List<Feedback> getAllFeedback(FeedbackType type);
    int getRequestCountByPlayer(Player player, FeedbackType type);

    boolean isFeedbackAlreadyExists(String idea);
}

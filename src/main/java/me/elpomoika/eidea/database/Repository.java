package me.elpomoika.eidea.database;

import me.elpomoika.eidea.models.IdeaModel;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Repository {
    void addPlayer(Player player, String idea);

    void addPlayerPremium(Player player, String idea);

    String getPlayer(int id);
    String getStatus(int id);
    void updateStatus(int id, String status);
    List<IdeaModel> getAllIdeas();
    int getRequestCountByPlayer(Player player);

    boolean wasIdeaReject(UUID uuid);

}

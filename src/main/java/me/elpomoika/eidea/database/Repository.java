package me.elpomoika.eidea.database;

import me.elpomoika.eidea.models.Idea;
import org.bukkit.entity.Player;

import java.util.List;

public interface Repository {
    void addPlayer(Player player, String idea);

    void addPlayerPremium(Player player, String idea);

    String getPlayer(int id);
    String getStatus(int id);
    void updateStatus(int id, byte status);
    List<Idea> getAllIdeas();
    int getRequestCountByPlayer(Player player);

}

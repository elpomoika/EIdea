package me.elpomoika.eidea.models;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@ToString
public enum IdeaStatus {
    APPROVED((byte) 0),
    DECLINED((byte) 1),
    PENDING((byte) 2);

    @Getter
    private final byte id;
    @Getter
    private String displayName;
    private static final Map<Byte, IdeaStatus> BY_ID = new HashMap<>();

    static {
        for (IdeaStatus status : values()) {
            BY_ID.put(status.id, status);
        }
    }

    IdeaStatus(byte id) {
        this.id = id;
        this.displayName = this.name();
    }

    public static void loadFromConfig(JavaPlugin plugin) {
        for (IdeaStatus status : values()) {
            String path = "idea-status." + status.name();
            if (plugin.getConfig().contains(path)) {
                status.displayName = ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString(path));
            }
        }
    }

    public static IdeaStatus fromId(byte id) {
        for (IdeaStatus status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown IdeaStatus id: " + id);
    }
}

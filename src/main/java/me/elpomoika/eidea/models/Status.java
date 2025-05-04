package me.elpomoika.eidea.models;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@ToString
public enum Status {
    APPROVED((byte) 0),
    DECLINED((byte) 1),
    PENDING((byte) 2);

    @Getter
    private final byte id;
    @Getter
    private String ideaDisplayName;
    @Getter
    private String bugDisplayName;
    private static final Map<Byte, Status> BY_ID = new HashMap<>();

    static {
        for (Status status : values()) {
            BY_ID.put(status.id, status);
            status.ideaDisplayName = status.name();
            status.bugDisplayName = status.name();
        }
    }

    Status(byte id) {
        this.id = id;
    }

    public String getDisplayName(FeedbackType type) {
        return type == FeedbackType.IDEA ? ideaDisplayName : bugDisplayName;
    }

    public static void loadFromConfig(JavaPlugin plugin) {
        for (Status status : values()) {
            String ideaPath = "status.idea." + status.name();
            if (plugin.getConfig().contains(ideaPath)) {
                status.ideaDisplayName = ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString(ideaPath));
            }

            String bugPath = "status.bug." + status.name();
            if (plugin.getConfig().contains(bugPath)) {
                status.bugDisplayName = ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString(bugPath));
            }
        }
    }

    public static Status fromId(byte id) {
        return BY_ID.get(id);
    }
}

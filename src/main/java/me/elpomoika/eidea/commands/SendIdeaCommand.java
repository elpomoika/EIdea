package me.elpomoika.eidea.commands;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.util.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class SendIdeaCommand implements CommandExecutor {
    private final MysqlRepository repository;
    private final CooldownManager manager;
    private final EIdea plugin;

    public SendIdeaCommand(MysqlRepository repository, CooldownManager manager, EIdea plugin) {
        this.repository = repository;
        this.manager = manager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player player = (Player) sender;
        final String message = String.join(" ", args);

        if (!(sender instanceof Player)) return false;

        if (message.trim().length() >= 120) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("too-long-idea")));
        }

        if (message.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("idea-is-empty-message")));
        }

        if (player.hasPermission("eidea.premium")) {
            repository.addPlayerPremium(player, message);
            return true;
        } else if (player.hasPermission("eidea.default")) {
            checkPlayerHasCooldown(player);

            repository.addPlayer(player, message);
            return true;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission-message")));
        return false;
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        return String.format("%dh %02dm %02ds",
                seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

    private boolean checkPlayerHasCooldown(Player player) {
        if (manager.hasCooldown(player.getUniqueId())) {
            Duration timeLeft = manager.getRemainingCooldown(player.getUniqueId());
            String timeFormatted = formatDuration(timeLeft);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("cooldown-message")
                            .replace("%remaining_time%", timeFormatted)));

            return true;
        }
        return false;
    }
}

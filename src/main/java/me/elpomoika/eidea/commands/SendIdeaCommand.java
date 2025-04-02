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
        if (!(sender instanceof Player)) return false;
        String message = String.join(" ", args);
        //TODO сделать проверку на длинну
        if (message.isEmpty()) {
            sender.sendMessage(plugin.getConfig().getString("idea-is-empty-message"));
        }

        Player player = (Player) sender;
        if (player.hasPermission("eidea.premium")) {
            repository.addPlayerPremium(player, message);
            return true;

        } else if (player.hasPermission("eidea.default")) {

            if (manager.hasCooldown(player.getUniqueId())) {
                Duration timeLeft = manager.getRemainingCooldown(player.getUniqueId());
                String timeFormatted = formatDuration(timeLeft);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("cooldown-message")
                                .replace("%remaining_time%", timeFormatted)));

                return true;
            }
            repository.addPlayer(player, message);
            return true;
        }
        player.sendMessage(plugin.getConfig().getString("no-permission-message"));
        return false;
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        return String.format("%dh %02dm %02ds",
                seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }
}

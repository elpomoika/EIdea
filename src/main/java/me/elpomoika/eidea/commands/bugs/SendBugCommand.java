package me.elpomoika.eidea.commands.bugs;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.factories.RepositoriesFactory;
import me.elpomoika.eidea.models.FeedbackType;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.util.cooldown.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class SendBugCommand implements CommandExecutor {
    private final CooldownManager manager;
    private final FeedbackMaster plugin;
    protected final Repository repository;
    protected final RepositoriesFactory repositoriesFactory;

    public SendBugCommand(CooldownManager manager, FeedbackMaster plugin) {
        repositoriesFactory = new RepositoriesFactory(plugin, new BukkitConfigProvider(plugin));
        repository = repositoriesFactory.getRepository(plugin.getConfig().getString("database.type"));
        this.manager = manager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        final Player player = (Player) sender;
        final String message = String.join(" ", args);

        if (message.trim().length() >= 200) {
            sendConfigMessage(player, "message.bug.too-long-bug");
            return true;
        }

        if (message.isEmpty()) {
            sendConfigMessage(player, "message.bug.bug-is-empty-message");
            return true;
        }

        if (player.hasPermission("eidea.premium")) {
            repository.addPlayerPremium(player, message, FeedbackType.BUG);
            return true;
        } else if (player.hasPermission("eidea.default")) {
            if (!checkPlayerHasCooldown(player)) {
                manager.setCooldown(player.getUniqueId(), Duration.ofSeconds(plugin.getConfig().getLong("cooldown", 10)));

                repository.addPlayer(player, message, FeedbackType.BUG);
                return true;
            }
            return true;
        }
        sendConfigMessage(player, "message.bug.no-permission-message");
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
            String message = plugin.getConfig().getString("message.bug.cooldown-message").replace("%remaining_time%", timeFormatted);

            sendConfigMessage(player, message);

            return true;
        }
        return false;
    }

    private void sendConfigMessage(Player player, String configPath) {
        String message = plugin.getConfig().getString(configPath);
        if (message != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}

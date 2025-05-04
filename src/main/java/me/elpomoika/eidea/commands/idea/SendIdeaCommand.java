package me.elpomoika.eidea.commands.idea;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.factories.RepositoriesFactory;
import me.elpomoika.eidea.models.FeedbackType;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.util.cooldown.IdeaCooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class SendIdeaCommand implements CommandExecutor {
    private final IdeaCooldownManager manager;
    private final FeedbackMaster plugin;
    protected final Repository repository;
    protected final RepositoriesFactory repositoriesFactory;

    public SendIdeaCommand(IdeaCooldownManager manager, FeedbackMaster plugin) {
        repositoriesFactory = new RepositoriesFactory(plugin, new BukkitConfigProvider(plugin));
        repository = repositoriesFactory.getRepository(plugin.getConfig().getString("database.type"));
        this.manager = manager;
        this.plugin = plugin;
    }

    //TODO fix methods below
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player player = (Player) sender;
        final String message = String.join(" ", args);

        if (!(sender instanceof Player)) return false;

        if (message.trim().length() >= 120) {
            sendConfigMessage(player, "message.idea.too-long-idea");
            return true;
        }

        if (message.isEmpty()) {
            sendConfigMessage(player, "message.idea.idea-is-empty-message");
            return true;
        }

        if (player.hasPermission("feedback.premium")) {
            repository.addPlayerPremium(player, message, FeedbackType.IDEA);
            return true;
        } else if (player.hasPermission("feedback.default")) {
            if (!checkPlayerHasCooldown(player)) {
                manager.setCooldown(player.getUniqueId(), Duration.ofSeconds(plugin.getConfig().getLong("cooldown", 10)));

                repository.addPlayer(player, message, FeedbackType.IDEA);
                return true;
            }
            return true;
        }
        sendConfigMessage(player, "message.idea.no-permission-message");
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
                    plugin.getConfig().getString("message.idea.cooldown-message")
                            .replace("%remaining_time%", timeFormatted)));

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

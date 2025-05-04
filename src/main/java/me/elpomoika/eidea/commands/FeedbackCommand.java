package me.elpomoika.eidea.commands;

import me.elpomoika.eidea.FeedbackMaster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class FeedbackCommand implements CommandExecutor {

    private final FeedbackMaster plugin;

    public FeedbackCommand(FeedbackMaster plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.GREEN + "FeedbackMaster successfully reload");
            plugin.reloadConfig();
        }

        return true;
    }
}

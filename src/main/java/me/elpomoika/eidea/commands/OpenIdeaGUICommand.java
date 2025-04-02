package me.elpomoika.eidea.commands;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.util.inventory.IdeaGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenIdeaGUICommand implements CommandExecutor {
    private final MysqlRepository mysqlRepository;
    private final EIdea plugin;

    public OpenIdeaGUICommand(MysqlRepository mysqlRepository, EIdea plugin) {
        this.mysqlRepository = mysqlRepository;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (player.hasPermission("eidea.admin")) {
            final IdeaGUI ideaGUI = new IdeaGUI(mysqlRepository);

            player.openInventory(ideaGUI.getInventory());
            return true;
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("no-permission-message")));
            return true;
        }
}
}

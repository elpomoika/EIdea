package me.elpomoika.eidea.commands;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.util.future.menus.PendingIdeasMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.elpomoika.inventoryapi.InventoryApi;

import java.util.Objects;

public class OpenGuiCommand implements CommandExecutor {
    private final MysqlRepository repository;
    private final InventoryApi api;
    private final EIdea plugin;

    public OpenGuiCommand(MysqlRepository repository, InventoryApi api, EIdea plugin) {
        this.repository = Objects.requireNonNull(repository, "MysqlRepository cannot be null");
        this.api = Objects.requireNonNull(api, "InventoryApi cannot be null");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) return false;
        if (!(player.hasPermission("eidea.admin"))) return false;

        PendingIdeasMenu inventory = new PendingIdeasMenu(api, repository, plugin);
        inventory.init();

        inventory.open(player);
        return true;
    }
}

package me.elpomoika.eidea.commands;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.factories.RepositoriesFactory;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.IdeaStatus;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.util.future.menus.PendingIdeasMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.elpomoika.inventoryapi.InventoryApi;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OpenGuiCommand implements CommandExecutor {
    private final InventoryApi api;
    private final EIdea plugin;
    protected Repository repository;
    protected RepositoriesFactory repositoriesFactory;

    public OpenGuiCommand(InventoryApi api, EIdea plugin) {
        repositoriesFactory = new RepositoriesFactory(plugin, new BukkitConfigProvider(plugin));
        repository = repositoriesFactory.getRepository(plugin.getConfig().getString("database.type"));
        this.api = Objects.requireNonNull(api, "InventoryApi cannot be null");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] strings) {
        Player player = (Player) sender;

        if (!(player.hasPermission("eidea.admin"))) return false;

        PendingIdeasMenu inventory = new PendingIdeasMenu(api, plugin);
        inventory.init();

        inventory.open(player);
        return true;
    }
}

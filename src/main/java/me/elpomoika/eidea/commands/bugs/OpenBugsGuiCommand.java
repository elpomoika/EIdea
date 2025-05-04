package me.elpomoika.eidea.commands.bugs;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.factories.RepositoriesFactory;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.feature.bug.menus.PendingBugsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.elpomoika.inventoryapi.InventoryApi;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OpenBugsGuiCommand implements CommandExecutor {
    private final InventoryApi api;
    private final FeedbackMaster plugin;
    protected Repository repository;
    protected RepositoriesFactory repositoriesFactory;

    public OpenBugsGuiCommand(InventoryApi api, FeedbackMaster plugin) {
        repositoriesFactory = new RepositoriesFactory(plugin, new BukkitConfigProvider(plugin));
        repository = repositoriesFactory.getRepository(plugin.getConfig().getString("database.type"));
        this.api = Objects.requireNonNull(api, "InventoryApi cannot be null");
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) sender;

        if (!(player.hasPermission("eidea.admin"))) return false;

        PendingBugsMenu ideasMenu = new PendingBugsMenu(api, plugin);

        ideasMenu.init();
        ideasMenu.open(player);
        return true;
    }
}

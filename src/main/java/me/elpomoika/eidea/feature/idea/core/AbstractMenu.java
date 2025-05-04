package me.elpomoika.eidea.feature.idea.core;

import lombok.Getter;
import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.factories.RepositoriesFactory;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import org.bukkit.entity.Player;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public abstract class AbstractMenu {
    @Getter
    private final InventoryApi api;
    protected NormalInventory inventory;
    protected final FeedbackMaster plugin;
    protected Repository repository;
    protected RepositoriesFactory repositoriesFactory;

    public AbstractMenu(InventoryApi api, FeedbackMaster plugin) {
        repositoriesFactory = new RepositoriesFactory(plugin, new BukkitConfigProvider(plugin));
        repository = repositoriesFactory.getRepository(plugin.getConfig().getString("database.type"));

        this.api = api;
        this.plugin = plugin;
        this.inventory = createInventory();
    }

    public void open(Player player) {
        inventory.open(player);
    }

    public void init() {
        initializeItems();
    }

    public abstract NormalInventory createInventory();
    protected abstract void initializeItems();
}

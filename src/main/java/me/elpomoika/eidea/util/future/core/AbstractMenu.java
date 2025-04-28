package me.elpomoika.eidea.util.future.core;

import lombok.Getter;
import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.factories.DatabaseFactory;
import me.elpomoika.eidea.database.factories.RepositoriesFactory;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public abstract class AbstractMenu {
    @Getter
    private final InventoryApi api;
    protected NormalInventory inventory;
    protected final EIdea plugin;
    protected Repository repository;
    protected RepositoriesFactory repositoriesFactory;

    public AbstractMenu(InventoryApi api, EIdea plugin) {
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

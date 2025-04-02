package me.elpomoika.eidea.util.future;

import lombok.Getter;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import org.bukkit.entity.Player;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public abstract class AbstractMenu {
    @Getter
    private final InventoryApi api;
    protected NormalInventory inventory;
    protected final MysqlRepository repository;

    public AbstractMenu(InventoryApi api, MysqlRepository repository) {
        this.api = api;
        this.repository = repository;
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

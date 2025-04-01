package me.elpomoika.eidea.util.future;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public abstract class AbstractMenu {
    @Getter
    private final InventoryApi api;
    protected NormalInventory inventory;

    protected AbstractMenu(InventoryApi api) {
        this.api = api;
        this.inventory = createInventory();
        initializeItems();
    }

    public void open(Player player) {
        inventory.open(player);
    }

    public abstract NormalInventory createInventory();
    protected abstract void initializeItems();
}

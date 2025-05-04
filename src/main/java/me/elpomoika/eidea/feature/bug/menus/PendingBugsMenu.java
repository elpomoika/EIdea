package me.elpomoika.eidea.feature.bug.menus;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.feature.bug.core.BugsMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

public class PendingBugsMenu extends BugsMenu {

    public PendingBugsMenu(InventoryApi api, FeedbackMaster plugin) {
        super(api, plugin, Status.PENDING);
    }

    @Override
    public NormalInventory createInventory() {
        NormalInventory normalInventory = new NormalInventory(getApi(), plugin.getConfig().getString("pending-ideas-menu.title") ,54);

        normalInventory.setItem(45, createApprovedFilterItem())
                .setClickHandler(45, event -> {
                    Player player = (Player) event.getWhoClicked();

                    OnlyApprovedBugsMenu onlyApprovedBugsMenu = new OnlyApprovedBugsMenu(getApi(), plugin);

                    onlyApprovedBugsMenu.init();
                    onlyApprovedBugsMenu.open(player);
                });

        normalInventory.setItem(53, createDeclinedFilterItem())
                .setClickHandler(53, event -> {
                    Player player = (Player) event.getWhoClicked();

                    OnlyDeclinedBugsMenu onlyDeclinedBugsMenu = new OnlyDeclinedBugsMenu(getApi(), plugin);

                    onlyDeclinedBugsMenu.init();
                    onlyDeclinedBugsMenu.open(player);
                });

        return normalInventory;
    }

    private ItemStack createApprovedFilterItem() {
        ItemStack item = new ItemBuilder(Material.ARROW)
                .setDisplayName("Go to only approved ideas")
                .setAmount(1)
                .build();
        return item;
    }

    private ItemStack createDeclinedFilterItem() {
        ItemStack item = new ItemBuilder(Material.ARROW)
                .setDisplayName("Go to only declined ideas")
                .setAmount(1)
                .build();
        return item;
    }
}

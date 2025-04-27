package me.elpomoika.eidea.util.future.menus;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.IdeaStatus;
import me.elpomoika.eidea.util.future.core.IdeasMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

public class PendingIdeasMenu extends IdeasMenu {

    public PendingIdeasMenu(InventoryApi api, EIdea plugin) {
        super(api, plugin, IdeaStatus.PENDING);
    }

    @Override
    public NormalInventory createInventory() {
        NormalInventory normalInventory = new NormalInventory(getApi(), plugin.getConfig().getString("pending-ideas-menu.title") ,54);

        normalInventory.setItem(45, createApprovedFilterItem())
                .setClickHandler(45, event -> {
                    Player player = (Player) event.getWhoClicked();

                    OnlyApproveMenu onlyApproveMenu = new OnlyApproveMenu(getApi(), plugin);

                    onlyApproveMenu.init();
                    onlyApproveMenu.open(player);
                });

        normalInventory.setItem(53, createDeclinedFilterItem())
                .setClickHandler(53, event -> {
                    Player player = (Player) event.getWhoClicked();

                    OnlyDeclinedMenu onlyApproveMenu = new OnlyDeclinedMenu(getApi(), plugin);

                    onlyApproveMenu.init();
                    onlyApproveMenu.open(player);
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

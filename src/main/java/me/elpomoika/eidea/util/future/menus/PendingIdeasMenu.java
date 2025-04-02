package me.elpomoika.eidea.util.future;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.IdeaStatus;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

public class PendingIdeasMenu extends IdeasMenu {
    private final EIdea plugin;

    public PendingIdeasMenu(InventoryApi api, MysqlRepository repository, EIdea plugin) {
        super(api, repository, plugin, IdeaStatus.PENDING);
        this.plugin = plugin;
    }

    @Override
    public NormalInventory createInventory() {
        NormalInventory normalInventory = new NormalInventory(getApi(), "Pending ideas", 54);

        normalInventory.setItem(45, createApprovedFilterItem())
                .setClickHandler(45, event -> {
                    Player player = (Player) event.getWhoClicked();

                    OnlyApproveMenu onlyApproveMenu = new OnlyApproveMenu(getApi(), repository, plugin);

                    onlyApproveMenu.init();
                    onlyApproveMenu.open(player);
                });

        normalInventory.setItem(53, createDeclinedFilterItem())
                .setClickHandler(53, event -> {
                    Player player = (Player) event.getWhoClicked();

                    OnlyDeclinedMenu onlyApproveMenu = new OnlyDeclinedMenu(getApi(), repository, plugin);

                    onlyApproveMenu.init();
                    onlyApproveMenu.open(player);
                });

        return normalInventory;
    }

    private ItemStack createApprovedFilterItem() {
        ItemStack item = new ItemBuilder(Material.ARROW)
                .setDisplayName("Переход на только одобренные идеи")
                .setAmount(1)
                .build();
        return item;
    }

    private ItemStack createDeclinedFilterItem() {
        ItemStack item = new ItemBuilder(Material.ARROW)
                .setDisplayName("Переход на только отклоненные идеи")
                .setAmount(1)
                .build();
        return item;
    }
}

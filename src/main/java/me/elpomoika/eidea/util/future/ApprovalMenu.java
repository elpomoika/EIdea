package me.elpomoika.eidea.util.future;

import lombok.Getter;
import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

public class ApprovalMenu extends AbstractMenu {

    public ApprovalMenu(InventoryApi api, MysqlRepository repository) {
        super(api, repository);
    }

    @Override
    public NormalInventory createInventory() {
        return new NormalInventory(getApi(), "Approval menu", 54);
    }

    @Override
    protected void initializeItems() {
        ItemStack approveItem = new ItemBuilder(Material.GREEN_WOOL)
                .setDisplayName("&aОдобрить")
                .setAmount(1)
                .build();

        ItemStack declineItem = new ItemBuilder(Material.RED_WOOL)
                .setDisplayName("&cОтклонить")
                .setAmount(1)
                .build();

        inventory.setItem(45, approveItem);
        inventory.setItem(53, declineItem);
    }
}

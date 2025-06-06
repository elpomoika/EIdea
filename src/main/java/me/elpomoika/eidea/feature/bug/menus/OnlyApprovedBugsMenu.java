package me.elpomoika.eidea.feature.bug.menus;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.feature.bug.core.BugsMenu;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public class OnlyApprovedBugsMenu extends BugsMenu {
    public OnlyApprovedBugsMenu(InventoryApi api, FeedbackMaster plugin) {
        super(api, plugin, Status.APPROVED);
    }

    @Override
    public NormalInventory createInventory() {
        NormalInventory normalInventory = new NormalInventory(getApi(), plugin.getConfig().getString("only-approve-menu.title"), 54);

        return normalInventory;
    }
}

package me.elpomoika.eidea.future.bugs.menus;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.future.bugs.core.BugsMenu;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public class OnlyDeclinedBugsMenu extends BugsMenu {
    public OnlyDeclinedBugsMenu(InventoryApi api, FeedbackMaster plugin) {
        super(api, plugin, Status.DECLINED);
    }

    @Override
    public NormalInventory createInventory() {
        return new NormalInventory(getApi(), plugin.getConfig().getString("only-declined-menu.title"), 54);
    }
}

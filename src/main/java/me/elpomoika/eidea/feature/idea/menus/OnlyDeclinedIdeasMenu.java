package me.elpomoika.eidea.feature.idea.menus;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.feature.idea.core.IdeasMenu;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public class OnlyDeclinedIdeasMenu extends IdeasMenu {

    public OnlyDeclinedIdeasMenu(InventoryApi api, FeedbackMaster plugin) {
        super(api, plugin, Status.DECLINED);
    }

    @Override
    public NormalInventory createInventory() {
        return new NormalInventory(getApi(), plugin.getConfig().getString("only-declined-menu.title"), 54);
    }
}

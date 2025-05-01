package me.elpomoika.eidea.util.future.menus;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.IdeaStatus;
import me.elpomoika.eidea.util.future.core.IdeasMenu;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public class OnlyDeclinedMenu extends IdeasMenu {

    public OnlyDeclinedMenu(InventoryApi api, EIdea plugin) {
        super(api, plugin, IdeaStatus.DECLINED);
    }

    @Override
    public NormalInventory createInventory() {
        return new NormalInventory(getApi(), plugin.getConfig().getString("only-declined-menu.title"), 54);
    }
}

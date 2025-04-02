package me.elpomoika.eidea.util.future;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.IdeaStatus;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;

public class OnlyApproveMenu extends IdeasMenu {
    private final EIdea plugin;

    public OnlyApproveMenu(InventoryApi api, MysqlRepository repository, EIdea plugin) {
        super(api, repository, plugin, IdeaStatus.APPROVED);
        this.plugin = plugin;
    }

    @Override
    public NormalInventory createInventory() {
        return new NormalInventory(getApi(), "Only Approve", 54);
    }
}

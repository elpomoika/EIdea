package me.elpomoika.eidea.util.future.menus;

import com.google.common.util.concurrent.MoreExecutors;
import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.IdeaStatus;
import me.elpomoika.eidea.util.future.core.IdeasMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

public class OnlyApproveMenu extends IdeasMenu {

    public OnlyApproveMenu(InventoryApi api, MysqlRepository repository, EIdea plugin) {
        super(api, repository, plugin, IdeaStatus.APPROVED);
    }

    @Override
    public NormalInventory createInventory() {
        NormalInventory normalInventory = new NormalInventory(getApi(), plugin.getConfig().getString("only-approve-menu.title"), 54);


        return normalInventory;
    }
}

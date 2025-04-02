package me.elpomoika.eidea.util.future;

import lombok.Getter;
import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.models.IdeaStatus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

public class ApprovalMenu extends AbstractMenu {
    @Getter
    private final int id;
    private final EIdea plugin;

    public ApprovalMenu(InventoryApi api, MysqlRepository repository, int id, EIdea plugin) {
        super(api, repository);
        this.id = id;
        this.plugin = plugin;
    }

    @Override
    public NormalInventory createInventory() {
        NormalInventory normalInventory = new NormalInventory(getApi(), "Approval menu", 54);
        return normalInventory;
    }

    @Override
    protected void initializeItems() {
        final String command = plugin.getConfig().getString("command-if-approved");
        ItemStack approveItem = new ItemBuilder(Material.GREEN_WOOL)
                .setDisplayName("&aОдобрить")
                .setAmount(1)
                .build();

        ItemStack declineItem = new ItemBuilder(Material.RED_WOOL)
                .setDisplayName("&cОтклонить")
                .setAmount(1)
                .build();

        inventory.setItem(45, approveItem).setClickHandler(45, event -> {
            Player player = (Player) event.getWhoClicked();
            repository.updateStatus(id, IdeaStatus.APPROVED.getDisplayName());

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player_name%", repository.getPlayer(id)));
            player.closeInventory();
        });
        inventory.setItem(53, declineItem).setClickHandler(53, event -> {
            Player player = (Player) event.getWhoClicked();

            repository.updateStatus(id, IdeaStatus.DECLINED.getDisplayName());

            player.closeInventory();
        });
    }
}

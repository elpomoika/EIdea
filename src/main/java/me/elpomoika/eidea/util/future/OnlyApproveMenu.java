package me.elpomoika.eidea.util.future;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.models.Idea;
import me.elpomoika.eidea.models.IdeaStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class OnlyApproveMenu extends AbstractMenu {
    private final EIdea plugin;

    public OnlyApproveMenu(InventoryApi api, MysqlRepository repository, EIdea plugin) {
        super(api, repository);
        this.plugin = plugin;
    }

    @Override
    public NormalInventory createInventory() {
        return new NormalInventory(getApi(), "Only Approve", 54);
    }

    @Override
    protected void initializeItems() {
        final List<Idea> ideas = new ArrayList<>();
        if (ideas.isEmpty()) return;

        int slot = 0;

        for (Idea idea : ideas) {
            if (!IdeaStatus.APPROVED.equalsIgnoreCase(idea.getStatus()));

            ItemStack item = new ItemBuilder(Material.PAPER)
                    .setDisplayName("Идея №" + idea.getId())
                    .setLore(Component.text("Автор: " + Bukkit.getPlayer(idea.getUuid()).getName()),
                            Component.text("Идея: " + idea.getIdea()),
                            Component.text("Статус: " + idea.getStatus()))
                    .setAmount(1)
                    .build();

            inventory.setItem(slot, item).setClickHandler(slot, event -> {
                Player player = (Player) event.getWhoClicked();
                ApprovalMenu approvalMenu = new ApprovalMenu(getApi(), repository, idea.getId(), plugin);
                approvalMenu.init();

                approvalMenu.open(player);
            });

            slot++;
        }
    }
}

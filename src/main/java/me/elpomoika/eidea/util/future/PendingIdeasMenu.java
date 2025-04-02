package me.elpomoika.eidea.util.future;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PendingIdeasMenu extends AbstractMenu {

    public PendingIdeasMenu(InventoryApi api, MysqlRepository repository) {
        super(api, repository);
    }

    @Override
    public NormalInventory createInventory() {
        return new NormalInventory(getApi(), "Pending ideas", 54);
    }

    @Override
    protected void initializeItems() {
        final List<Idea> ideas = this.repository.getAllIdeas();
        if (ideas == null || ideas.isEmpty()) {
            return;
        }

        int slot = 0;

        for (Idea idea : ideas) {
            if (!idea.getStatus().equalsIgnoreCase(IdeaStatus.PENDING.getDisplayName().toUpperCase())) continue;

            ItemStack item = new ItemBuilder(Material.PAPER)
                    .setDisplayName("Идея №" + idea.getId())
                    .setLore(Component.text("Автор: " + Bukkit.getPlayer(idea.getUuid()).getName()),
                            Component.text("Идея: " + idea.getIdea()),
                            Component.text("Статус: " + idea.getStatus()))
                    .setAmount(1)
                    .build();

            inventory.setItem(slot, item).setClickHandler(slot, event -> {
                Player player = (Player) event.getWhoClicked();
                ApprovalMenu approvalMenu = new ApprovalMenu(getApi(), repository);

                approvalMenu.open(player);
            });

            slot++;
        }
    }
}

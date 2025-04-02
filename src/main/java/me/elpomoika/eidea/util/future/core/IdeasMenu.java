package me.elpomoika.eidea.util.future;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.Idea;
import me.elpomoika.eidea.models.IdeaStatus;
import me.elpomoika.eidea.util.future.core.AbstractMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

import java.util.List;

public abstract class IdeasMenu extends AbstractMenu {
    protected final EIdea plugin;
    protected final IdeaStatus ideaStatus;

    public IdeasMenu(InventoryApi api, MysqlRepository repository, EIdea plugin, IdeaStatus ideaStatus) {
        super(api, repository);
        this.plugin = plugin;
        this.ideaStatus = ideaStatus;
    }

    @Override
    public NormalInventory createInventory() {
        return null;
    }

    @Override
    protected void initializeItems() {
        final List<Idea> ideas = repository.getAllIdeas();
        int slot = 0;

        for (Idea idea : ideas) {
            if (!ideaStatus.equalsIgnoreCase(idea.getStatus())) continue;
            createIdeaItem(idea);
            setupClickHandler(slot, idea);

            slot++;
        }
    }

    protected ItemStack createIdeaItem(Idea idea) {
        return new ItemBuilder(Material.PAPER)
                .setDisplayName("Айди: " + idea.getId())
                .setLore(
                        Component.text("Автор: " + Bukkit.getPlayer(idea.getUuid()).getName()),
                        Component.text("Идея: " + idea.getIdea()),
                        Component.text("Статус: " + idea.getStatus())
                )
                .setAmount(1)
                .build();
    }

    protected void setupClickHandler(int slot, Idea idea) {
        inventory.setItem(slot, createIdeaItem(idea))
                .setClickHandler(slot, event -> {
                    Player player = (Player) event.getWhoClicked();

                    ApprovalMenu approvalMenu = new ApprovalMenu(getApi(), repository, idea.getId(), plugin);

                    approvalMenu.init();
                    approvalMenu.open(player);
                });
    }
}

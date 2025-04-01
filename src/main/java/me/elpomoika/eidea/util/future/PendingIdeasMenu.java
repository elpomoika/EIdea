package me.elpomoika.eidea.util.future;

import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.models.Idea;
import me.elpomoika.eidea.models.IdeaStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

import java.util.List;
import java.util.Objects;

public class PendingIdeasMenu extends AbstractMenu {
    private final MysqlRepository repository;

    public PendingIdeasMenu(InventoryApi api, MysqlRepository repository) {
        super(api);
        this.repository = Objects.requireNonNull(repository, "MysqlRepository cannot be null hhahah");
    }

    @Override
    public NormalInventory createInventory() {
        return new NormalInventory(getApi(), "Pending ideas", 54);
    }

    @Override
    protected void initializeItems() {
        final List<Idea> ideas = this.repository.getAllIdeas();
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

            inventory.setItem(slot++, item);
        }
    }
}

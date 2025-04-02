package me.elpomoika.eidea.util.future;

import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.models.Idea;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

public abstract class IdeasMenu extends AbstractMenu {

    public IdeasMenu(InventoryApi api, MysqlRepository repository) {
        super(api, repository);
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

    @Override
    public NormalInventory createInventory() {
        return null;
    }

    @Override
    protected void initializeItems() {

    }
}

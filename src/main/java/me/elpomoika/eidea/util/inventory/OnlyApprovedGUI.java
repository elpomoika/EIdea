package me.elpomoika.eidea.util.inventory;

import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.Idea;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OnlyApprovedGUI implements InventoryHolder {
    private Inventory inventory;
    private final MysqlRepository repository;

    public OnlyApprovedGUI(MysqlRepository repository) {
        this.repository = repository;
        this.inventory = createInventory(0);

        initializeItems();
    }

    private void initializeItems() {
        List<Idea> ideas = repository.getAllIdeas();
        int slot = 0;
        int page = 0;

        for (Idea idea : ideas) {
            if (!idea.getStatus().equalsIgnoreCase("ОДОБРЕНО")) continue;

            ItemStack item = new ItemStack(Material.CREEPER_HEAD);
            ItemMeta itemMeta = item.getItemMeta();

            if (slot >= 45) {
                page++;
                inventory = createInventory(page);
                slot = 0;
            }

            int id = idea.getId();
            itemMeta.setDisplayName(ChatColor.GREEN + "Идея #" + id);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Автор: " + ChatColor.WHITE + Bukkit.getOfflinePlayer(idea.getUuid()).getName());
            lore.add(ChatColor.GRAY + "ID: " + id);
            lore.add(ChatColor.GRAY + "Статус: " + repository.getStatus(id));
            lore.add(ChatColor.GREEN + "Идея: " + idea.getIdea());
            itemMeta.setLore(lore);

            item.setItemMeta(itemMeta);
            inventory.setItem(slot++, item);
        }
    }

    private Inventory createInventory(int page) {
        return Bukkit.createInventory(this, 54, "Только одобреные - Страница " + (page + 1));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

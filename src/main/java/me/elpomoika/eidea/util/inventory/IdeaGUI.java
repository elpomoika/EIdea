package me.elpomoika.eidea.util.inventory;

import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.models.IdeaModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class IdeaGUI implements InventoryHolder {
    private Inventory inventory;
    private final MysqlRepository repository;
    private int id;

    public IdeaGUI(MysqlRepository repository) {
        this.inventory = createInventory(0);
        this.repository = repository;

        initializeItems();
    }

    private void initializeItems() {
        List<IdeaModel> ideas = repository.getAllIdeas();
        int slot = 0;
        int page = 0;

        for (IdeaModel idea : ideas) {
            if (!idea.getStatus().equalsIgnoreCase("PENDING")) continue;

            ItemStack items = new ItemStack(Material.CREEPER_HEAD);
            ItemMeta itemMeta = items.getItemMeta();
            id = idea.getId();

            if (slot >= 45) {
                page++;
                inventory = createInventory(page);
                slot = 0;

            }

            itemMeta.setDisplayName(ChatColor.GREEN + "Идея #" + id);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Автор: " + ChatColor.WHITE + Bukkit.getOfflinePlayer(idea.getUuid()).getName());
            lore.add(ChatColor.GRAY + "ID: " + id);
            lore.add(ChatColor.GRAY + "Статус: " + repository.getStatus(id));
            lore.add(ChatColor.GREEN + "Идея: " + idea.getIdea());
            itemMeta.setLore(lore);

            items.setItemMeta(itemMeta);
            inventory.setItem(slot++, items);
        }

        ItemStack goOnlyApprovedGUI = new ItemStack(Material.STONE_BUTTON);
        ItemMeta goOnlyApprovedGUIItemMeta = goOnlyApprovedGUI.getItemMeta();
        goOnlyApprovedGUIItemMeta.setDisplayName(ChatColor.GRAY + "Переход к только одобренным идеям");
        goOnlyApprovedGUI.setItemMeta(goOnlyApprovedGUIItemMeta);
        inventory.setItem(53, goOnlyApprovedGUI);

        ItemStack goOnlyDeclinedGUI = new ItemStack(Material.ARROW);
        ItemMeta goOnlyDeclinedGUIItemMeta = goOnlyDeclinedGUI.getItemMeta();
        goOnlyDeclinedGUIItemMeta.setDisplayName(ChatColor.GRAY + "Переход к только отклоненным идеям");
        goOnlyApprovedGUI.setItemMeta(goOnlyDeclinedGUIItemMeta);
        inventory.setItem(45, goOnlyDeclinedGUI);
    }


    private Inventory createInventory(int page) {
        return Bukkit.createInventory(this, 54, "Pending ideas - Страница " + (page + 1));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public int getId() {
        return id;
    }
}

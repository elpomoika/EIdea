package me.elpomoika.eidea.util.future;

import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.models.Idea;
import me.elpomoika.eidea.models.IdeaStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;

public class PendingMenu {
    private final List<Idea> pendingIdeas;
    private final MysqlRepository repository;

    public PendingMenu(Player player, MysqlRepository repository) {
        this.repository = repository;
        this.pendingIdeas = repository.getAllIdeas();
        createGUI(player);
    }

    public void createGUI(Player player) {
        Gui gui = Gui.empty(9, 6);
        int slot = 0;

        for (Idea idea : pendingIdeas) {
            if (!idea.getStatus().equalsIgnoreCase(IdeaStatus.PENDING.getDisplayName())) continue;
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN + "Автор: " + ChatColor.GRAY + Bukkit.getPlayer(idea.getUuid()).getName());
            lore.add(ChatColor.GREEN + "Идея: " + idea.getIdea());
            lore.add(ChatColor.GREEN + "Статус: " + idea.getStatus());
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + "Идея №" + idea.getId());
            meta.setLore(lore);
            itemStack.setItemMeta(meta);

            gui.setItem(slot++, new SimpleItem(itemStack));
        }
            
        Window window = Window.single()
                .setViewer(player)
                .setTitle("Идеи ожидающие вердикта")
                .setGui(gui)
                .build();
        window.open();
    }
}

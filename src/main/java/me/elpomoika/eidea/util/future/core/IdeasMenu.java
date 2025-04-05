package me.elpomoika.eidea.util.future.core;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.Idea;
import me.elpomoika.eidea.models.IdeaStatus;
import me.elpomoika.eidea.util.future.menus.ApprovalMenu;
import me.elpomoika.eidea.util.future.menus.PendingIdeasMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class IdeasMenu extends AbstractMenu {
    protected final IdeaStatus ideaStatus;

    public IdeasMenu(InventoryApi api, MysqlRepository repository, EIdea plugin, IdeaStatus ideaStatus) {
        super(api, plugin, repository);
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

        addBackItem();
    }

    protected ItemStack createIdeaItem(Idea idea) {
        return new ItemBuilder(Material.PAPER)
                .setDisplayName(plugin.getConfig().getString("idea-item.displayname").replace("%id%", String.valueOf(idea.getId())))
                .setLore(getIdeaLoreFromConfig(idea))
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

    private Component[] getIdeaLoreFromConfig(Idea idea) {
        List<String> loreLines = plugin.getConfig().getStringList("idea-item.lore");
        List<Component> result = new ArrayList<>();

        OfflinePlayer player = Bukkit.getOfflinePlayer(idea.getUuid());
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();

        for (String line : loreLines) {
            line = line.replace("%player%", player.getName())
                    .replace("%idea%", idea.getIdea())
                    .replace("%status%", idea.getStatus().toString());
            result.add(serializer.deserialize(line));
        }

        return result.toArray(new Component[0]);
    }

    private void addBackItem() {
        ItemStack itemStack = new ItemBuilder(Material.STONE_BUTTON)
                .setDisplayName(ChatColor.WHITE + "Вернуться в главное меню")
                .setAmount(1)
                .build();

        inventory.setItem(49, itemStack).setClickHandler(49, event -> {
            Player player = (Player) event.getWhoClicked();

            PendingIdeasMenu pendingIdeasMenu = new PendingIdeasMenu(getApi(), repository, plugin);
            pendingIdeasMenu.init();
            pendingIdeasMenu.open(player);
        });
    }
}

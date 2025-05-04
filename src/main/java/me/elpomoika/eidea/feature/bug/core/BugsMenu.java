package me.elpomoika.eidea.feature.bug.core;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.clip.placeholderapi.PlaceholderAPI;
import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.factories.RepositoriesFactory;
import me.elpomoika.eidea.models.Feedback;
import me.elpomoika.eidea.models.FeedbackType;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.util.HeadManager;
import me.elpomoika.eidea.feature.bug.menus.BugsApprovalMenu;
import me.elpomoika.eidea.feature.bug.menus.PendingBugsMenu;
import me.elpomoika.eidea.feature.idea.core.AbstractMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class BugsMenu extends AbstractMenu {

    protected final Status status;
    protected final Repository repository;
    protected final RepositoriesFactory repositoriesFactory;
    public static Cache<Integer, ItemStack> ideaItemCache;

    public BugsMenu(InventoryApi api, FeedbackMaster plugin, Status status) {
        super(api, plugin);
        repositoriesFactory = new RepositoriesFactory(plugin, new BukkitConfigProvider(plugin));
        repository = repositoriesFactory.getRepository(plugin.getConfig().getString("database.type"));
        this.status = status;

        ideaItemCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public NormalInventory createInventory() {
        return null;
    }

    @Override
    protected void initializeItems() {
        final List<Feedback> bugs = repository.getAllFeedback(FeedbackType.BUG);
        int slot = 0;

        for (Feedback bug : bugs) {
            if (bug.getStatus() != status) continue;
            if (bug.getType() != FeedbackType.BUG) continue;

            ItemStack item = ideaItemCache.get(
                    bug.getId(),
                    id -> createBugItem(bug)
            );

            inventory.setItem(slot, item);
            setupClickHandler(slot, bug);

            slot++;
        }

        addBackItem();
    }

    protected ItemStack createBugItem(Feedback feedback) {
        String materialName = plugin.getConfig().getString("bug-item.material", "PAPER");

        if (materialName.equalsIgnoreCase("player_head")) {
            if (this.plugin.getConfig().getString("bug-item.head-base64").isEmpty()) {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(feedback.getUuid()));
                head.setItemMeta(meta);
                return new ItemBuilder(head)
                        .setDisplayName(plugin.getConfig().getString("bug-item.displayname").replace("%id%", String.valueOf(feedback.getId())))
                        .setLore(getBugLoreFromConfig(feedback))
                        .setAmount(1)
                        .build();
            }
            return new ItemBuilder(HeadManager.createCustomHead(this.plugin.getConfig().getString("idea-item.head-base64")))
                    .setDisplayName(plugin.getConfig().getString("bug-item.displayname").replace("%id%", String.valueOf(feedback.getId())))
                    .setLore(getBugLoreFromConfig(feedback))
                    .setAmount(1)
                    .build();
        } else {
            Material material = Material.getMaterial(materialName.toUpperCase());
            return new ItemBuilder(material)
                    .setDisplayName(plugin.getConfig().getString("bug-item.displayname").replace("%id%", String.valueOf(feedback.getId())))
                    .setLore(getBugLoreFromConfig(feedback))
                    .setAmount(1)
                    .build();
        }
    }

    protected void setupClickHandler(int slot, Feedback bug) {
        inventory.setItem(slot, createBugItem(bug))
                .setClickHandler(slot, event -> {
                    Player player = (Player) event.getWhoClicked();

                    BugsApprovalMenu bugsApprovalMenu = new BugsApprovalMenu(getApi(), bug.getId(), plugin);

                    bugsApprovalMenu.init();
                    bugsApprovalMenu.open(player);
                });
    }

    private Component[] getBugLoreFromConfig(Feedback feedback) {
        List<String> loreLines = plugin.getConfig().getStringList("bug-item.lore");
        List<Component> result = new ArrayList<>();

        OfflinePlayer player = Bukkit.getOfflinePlayer(feedback.getUuid());

        for (String line : loreLines) {
            line = PlaceholderAPI.setPlaceholders(player, line.replace("%player%", player.getName())
                    .replace("%bug%", feedback.getIdea())
                    .replace("%status%", feedback.getStatus().getDisplayName(feedback.getType())));
            result.add(Component.text(ChatColor.translateAlternateColorCodes('&', line)));
        }

        return result.toArray(new Component[0]);
    }

    private void addBackItem() {
        ItemStack itemStack = new ItemBuilder(Material.STONE_BUTTON)
                .setDisplayName(ChatColor.WHITE + "Back to main menu")
                .setAmount(1)
                .build();

        inventory.setItem(49, itemStack).setClickHandler(49, event -> {
            Player player = (Player) event.getWhoClicked();

            PendingBugsMenu pendingBugsMenu = new PendingBugsMenu(getApi(), plugin);
            pendingBugsMenu.init();
            pendingBugsMenu.open(player);
        });
    }
}

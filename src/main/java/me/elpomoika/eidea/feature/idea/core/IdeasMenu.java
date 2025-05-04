package me.elpomoika.eidea.feature.idea.core;

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
import me.elpomoika.eidea.feature.idea.menus.IdeaApprovalMenu;
import me.elpomoika.eidea.feature.idea.menus.PendingIdeasMenu;
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

public abstract class IdeasMenu extends AbstractMenu {
    protected final Status status;
    protected Repository repository;
    protected RepositoriesFactory repositoriesFactory;
    public static Cache<Integer, ItemStack> ideaItemCache;

    public IdeasMenu(InventoryApi api, FeedbackMaster plugin, Status status) {
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
        final List<Feedback> feedbacks = repository.getAllFeedback(FeedbackType.IDEA);
        int slot = 0;

        for (Feedback feedback : feedbacks) {
            if (feedback.getStatus() != status) continue;
            if (feedback.getType() != FeedbackType.IDEA) continue;

            ItemStack item = ideaItemCache.get(
                    feedback.getId(),
                    id -> createIdeaItem(feedback)
            );

            inventory.setItem(slot, item);
            setupClickHandler(slot, feedback);

            slot++;
        }

        addBackItem();
    }

    protected ItemStack createIdeaItem(Feedback feedback) {
        String materialName = plugin.getConfig().getString("idea-item.material", "PAPER");

        if (materialName.equalsIgnoreCase("player_head")) {
            if (this.plugin.getConfig().getString("idea-item.head-base64").isEmpty()) {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(feedback.getUuid()));
                head.setItemMeta(meta);
                return new ItemBuilder(head)
                        .setDisplayName(plugin.getConfig().getString("idea-item.displayname").replace("%id%", String.valueOf(feedback.getId())))
                        .setLore(getIdeaLoreFromConfig(feedback))
                        .setAmount(1)
                        .build();
            }
            return new ItemBuilder(HeadManager.createCustomHead(this.plugin.getConfig().getString("idea-item.head-base64")))
                    .setDisplayName(plugin.getConfig().getString("idea-item.displayname").replace("%id%", String.valueOf(feedback.getId())))
                    .setLore(getIdeaLoreFromConfig(feedback))
                    .setAmount(1)
                    .build();
        } else {
            Material material = Material.getMaterial(materialName.toUpperCase());
            return new ItemBuilder(material)
                    .setDisplayName(plugin.getConfig().getString("idea-item.displayname").replace("%id%", String.valueOf(feedback.getId())))
                    .setLore(getIdeaLoreFromConfig(feedback))
                    .setAmount(1)
                    .build();
        }
    }

    protected void setupClickHandler(int slot, Feedback feedback) {
        inventory.setItem(slot, createIdeaItem(feedback))
                .setClickHandler(slot, event -> {
                    Player player = (Player) event.getWhoClicked();

                    IdeaApprovalMenu ideaApprovalMenu = new IdeaApprovalMenu(getApi(), feedback.getId(), plugin);

                    ideaApprovalMenu.init();
                    ideaApprovalMenu.open(player);
                });
    }

    private Component[] getIdeaLoreFromConfig(Feedback feedback) {
        List<String> loreLines = plugin.getConfig().getStringList("idea-item.lore");
        List<Component> result = new ArrayList<>();

        OfflinePlayer player = Bukkit.getOfflinePlayer(feedback.getUuid());

        for (String line : loreLines) {
            line = PlaceholderAPI.setPlaceholders(player, line.replace("%player%", player.getName())
                    .replace("%idea%", feedback.getIdea())
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

            PendingIdeasMenu pendingIdeasMenu = new PendingIdeasMenu(getApi(), plugin);
            pendingIdeasMenu.init();
            pendingIdeasMenu.open(player);
        });
    }
}

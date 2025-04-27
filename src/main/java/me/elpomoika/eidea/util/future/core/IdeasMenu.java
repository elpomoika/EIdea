package me.elpomoika.eidea.util.future.core;

import me.clip.placeholderapi.PlaceholderAPI;
import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.factories.RepositoriesFactory;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.models.Idea;
import me.elpomoika.eidea.models.IdeaStatus;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.util.HeadManager;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class IdeasMenu extends AbstractMenu {
    protected final IdeaStatus ideaStatus;
    protected Repository repository;
    protected RepositoriesFactory repositoriesFactory;

    public IdeasMenu(InventoryApi api, EIdea plugin, IdeaStatus ideaStatus) {
        super(api, plugin);
        repositoriesFactory = new RepositoriesFactory(plugin, new BukkitConfigProvider(plugin));
        repository = repositoriesFactory.getRepository(plugin.getConfig().getString("database-type"));
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
        String materialName = plugin.getConfig().getString("idea-item.material", "PAPER");

        if (materialName.equalsIgnoreCase("player_head")) {
            if (this.plugin.getConfig().getString("idea-item.head-base64").isEmpty()) {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) head.getItemMeta();
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(idea.getUuid()));
                head.setItemMeta(meta);
                return new ItemBuilder(head)
                        .setDisplayName(plugin.getConfig().getString("idea-item.displayname").replace("%id%", String.valueOf(idea.getId())))
                        .setLore(getIdeaLoreFromConfig(idea))
                        .setAmount(1)
                        .build();
            }
            return new ItemBuilder(HeadManager.createCustomHead(this.plugin.getConfig().getString("idea-item.head-base64")))
                    .setDisplayName(plugin.getConfig().getString("idea-item.displayname").replace("%id%", String.valueOf(idea.getId())))
                    .setLore(getIdeaLoreFromConfig(idea))
                    .setAmount(1)
                    .build();
        } else {
            Material material = Material.getMaterial(materialName.toUpperCase());
            return new ItemBuilder(material)
                    .setDisplayName(plugin.getConfig().getString("idea-item.displayname").replace("%id%", String.valueOf(idea.getId())))
                    .setLore(getIdeaLoreFromConfig(idea))
                    .setAmount(1)
                    .build();
        }
    }

    protected void setupClickHandler(int slot, Idea idea) {
        inventory.setItem(slot, createIdeaItem(idea))
                .setClickHandler(slot, event -> {
                    Player player = (Player) event.getWhoClicked();

                    ApprovalMenu approvalMenu = new ApprovalMenu(getApi(), idea.getId(), plugin);

                    approvalMenu.init();
                    approvalMenu.open(player);
                });
    }

    private Component[] getIdeaLoreFromConfig(Idea idea) {
        List<String> loreLines = plugin.getConfig().getStringList("idea-item.lore");
        List<Component> result = new ArrayList<>();

        OfflinePlayer player = Bukkit.getOfflinePlayer(idea.getUuid());

        for (String line : loreLines) {
            line = PlaceholderAPI.setPlaceholders(player, line.replace("%player%", player.getName())
                    .replace("%idea%", idea.getIdea())
                    .replace("%status%", idea.getStatus().toString()));
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

package me.elpomoika.eidea.feature.idea.menus;

import lombok.Getter;
import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.util.cooldown.IdeaCooldownManager;
import me.elpomoika.eidea.feature.idea.core.AbstractMenu;
import me.elpomoika.eidea.feature.idea.core.IdeasMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.elpomoika.inventoryapi.InventoryApi;
import org.elpomoika.inventoryapi.inventory.NormalInventory;
import org.elpomoika.inventoryapi.item.ItemBuilder;

public class IdeaApprovalMenu extends AbstractMenu {
    @Getter
    private final int id;
    private final IdeaCooldownManager manager;

    public IdeaApprovalMenu(InventoryApi api, int id, FeedbackMaster plugin) {
        super(api, plugin);
        this.id = id;
        this.manager = new IdeaCooldownManager();
    }

    @Override
    public NormalInventory createInventory() {
        NormalInventory normalInventory = new NormalInventory(getApi(), this.plugin.getConfig().getString("approval-menu.title"), 27);
        return normalInventory;
    }

    @Override
    protected void initializeItems() {
        final String command = plugin.getConfig().getString("command-if-approved");

        createApproveItem();
        createDeclineItem();
        inventory.setItem(11, createApproveItem()).setClickHandler(11, event -> {
            final Player player = (Player) event.getWhoClicked();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                repository.updateStatus(id, Status.APPROVED.getId(), manager);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    IdeasMenu.ideaItemCache.invalidate(id);
                    player.closeInventory();
                });
            });

            if (!command.isEmpty()) {
                final String playerName = repository.getPlayer(id);

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player_name%", playerName));
            }
        });

        inventory.setItem(15, createDeclineItem()).setClickHandler(15, event -> {
            Player player = (Player) event.getWhoClicked();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                repository.updateStatus(id, Status.DECLINED.getId(), manager);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    IdeasMenu.ideaItemCache.invalidate(id);
                    player.closeInventory();
                });
            });
        });

        addBackItem();
    }

    private void addBackItem() {
        ItemStack itemStack = new ItemBuilder(Material.STONE_BUTTON)
                .setDisplayName(ChatColor.WHITE + "Back to main menu")
                .setAmount(1)
                .build();

        inventory.setItem(18, itemStack).setClickHandler(18, event -> {
            Player player = (Player) event.getWhoClicked();

            PendingIdeasMenu pendingIdeasMenu = new PendingIdeasMenu(getApi(), plugin);
            pendingIdeasMenu.init();
            pendingIdeasMenu.open(player);
        });
    }

    private ItemStack createApproveItem() {
        ItemStack approveItem = new ItemBuilder(Material.GREEN_WOOL)
                .setDisplayName("&aApprove")
                .setAmount(1)
                .build();

        return approveItem;
    }

    private ItemStack createDeclineItem() {
        ItemStack declineItem = new ItemBuilder(Material.RED_WOOL)
                .setDisplayName("&cDecline")
                .setAmount(1)
                .build();

        return declineItem;
    }
}

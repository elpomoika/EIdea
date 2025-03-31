package me.elpomoika.eidea.listeners;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.util.inventory.ApproveDeclineGUI;
import me.elpomoika.eidea.util.inventory.IdeaGUI;
import me.elpomoika.eidea.util.inventory.OnlyApprovedGUI;
import me.elpomoika.eidea.util.inventory.OnlyDeclineGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
    private final MysqlRepository repository;
    private final EIdea plugin;

    public GUIListener(MysqlRepository repository, EIdea plugin) {
        this.repository = repository;
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickIdeaGUI(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        Player player = (Player) event.getWhoClicked();

        if (inventory.getHolder() instanceof IdeaGUI) {
            if (clickedItem.getType() == Material.CREEPER_HEAD) {
                event.setCancelled(true);

                ApproveDeclineGUI approveDeclineGUI = new ApproveDeclineGUI();
                player.openInventory(approveDeclineGUI.getInventory());
            } else if (clickedItem.getType() == Material.STONE_BUTTON) {
                event.setCancelled(true);

                OnlyApprovedGUI onlyApprovedGUI = new OnlyApprovedGUI(repository);
                player.openInventory(onlyApprovedGUI.getInventory());
            } else if (clickedItem.getType() == Material.ARROW) {
                event.setCancelled(true);

                OnlyDeclineGUI onlyDeclineGUI = new OnlyDeclineGUI(repository);
                player.openInventory(onlyDeclineGUI.getInventory());
            }
        }
    }

    @EventHandler
    public void onClickApproveGUI(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        IdeaGUI ideaGUI = new IdeaGUI(repository);

        if (inventory.getHolder() instanceof ApproveDeclineGUI) {
            event.setCancelled(true);
            String command = plugin.getConfig().getString("command-if-approved");

            if (clickedItem.getType() == Material.GREEN_WOOL) {
                repository.updateStatus(ideaGUI.getId(), "ОДОБРЕНО");
                player.closeInventory();
                command = command.replace("%player_name%", repository.getPlayer(ideaGUI.getId()));
                if (command != null && !command.isEmpty()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("approve-message")));
            } else if (clickedItem.getType() == Material.RED_WOOL) {
                repository.updateStatus(ideaGUI.getId(), "ОТКЛОНЕНО");
                player.closeInventory();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("decline-message")));
            }
        }
    }

    @EventHandler
    public void onClickOnlyApprove(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        if (inventory.getHolder() instanceof OnlyApprovedGUI) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickOnlyDeclined(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        if (inventory.getHolder() instanceof OnlyDeclineGUI) {
            event.setCancelled(true);
        }
    }
}

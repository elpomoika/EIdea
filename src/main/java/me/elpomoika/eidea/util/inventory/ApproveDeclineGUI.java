package me.elpomoika.eidea.util.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApproveDeclineGUI implements InventoryHolder {
    private final Inventory inventory;

    public ApproveDeclineGUI() {
        this.inventory = Bukkit.createInventory(this, 27, "Меню одобрения");

        initializeItems();
    }

    private void initializeItems() {
        ItemStack approve = new ItemStack(Material.GREEN_WOOL);
        ItemMeta approveMeta = approve.getItemMeta();

        approveMeta.setDisplayName(ChatColor.GREEN + "Одобрить");
        approve.setItemMeta(approveMeta);

        ItemStack decline = new ItemStack(Material.RED_WOOL);
        ItemMeta declineMeta = decline.getItemMeta();

        declineMeta.setDisplayName(ChatColor.RED + "Отклонить");
        decline.setItemMeta(declineMeta);

        inventory.setItem(10, approve);
        inventory.setItem(15, decline);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

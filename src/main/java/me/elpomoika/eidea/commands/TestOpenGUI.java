package me.elpomoika.eidea.commands;

import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.util.future.ApprovalMenu;
import me.elpomoika.eidea.util.future.PendingIdeasMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.elpomoika.inventoryapi.InventoryApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TestOpenGUI implements CommandExecutor {
    private final MysqlRepository repository;
    private final InventoryApi api;

    public TestOpenGUI(MysqlRepository repository, InventoryApi api) {
        this.repository = Objects.requireNonNull(repository, "MysqlRepository cannot be null");
        this.api = Objects.requireNonNull(api, "InventoryApi cannot be null");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        PendingIdeasMenu inventory = new PendingIdeasMenu(this.api, this.repository);

        inventory.open(player);
        return true;
    }
}

package me.elpomoika.eidea.commands;

import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.util.future.PendingMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestOpenGUI implements CommandExecutor {
    private final MysqlRepository repository;
    private PendingMenu pendingMenu;

    public TestOpenGUI(MysqlRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) sender;
        this.pendingMenu = new PendingMenu(player, repository);
        pendingMenu.createGUI(player);

        return true;
    }
}

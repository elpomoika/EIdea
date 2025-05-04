package me.elpomoika.eidea;

import lombok.Getter;
import me.elpomoika.eidea.commands.FeedbackCommand;
import me.elpomoika.eidea.commands.bugs.OpenBugsGuiCommand;
import me.elpomoika.eidea.commands.bugs.SendBugCommand;
import me.elpomoika.eidea.commands.idea.OpenIdeasGuiCommand;
import me.elpomoika.eidea.commands.idea.SendIdeaCommand;
import me.elpomoika.eidea.database.DatabaseConnection;
import me.elpomoika.eidea.database.factories.DatabaseFactory;
import me.elpomoika.eidea.models.Status;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.util.cooldown.BugsCooldownManager;
import me.elpomoika.eidea.util.cooldown.IdeaCooldownManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.elpomoika.inventoryapi.InventoryApi;

import java.sql.SQLException;

public final class FeedbackMaster extends JavaPlugin {
    @Getter
    private final IdeaCooldownManager ideaCooldownManager = new IdeaCooldownManager();
    @Getter
    private final BugsCooldownManager bugsCooldownManager = new BugsCooldownManager();
    private InventoryApi api;
    private DatabaseConnection data;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        DatabaseFactory databaseFactory = new DatabaseFactory(this, new BukkitConfigProvider(this));
        Status.loadFromConfig(this);

        try {
            data = databaseFactory.getDatabaseConnection(getConfig().getString("database.type"));
            data.getConnection();
            data.createTable();

            this.api = new InventoryApi(this);
        } catch (SQLException e) {
            getLogger().warning("ERROR IN FeedbackMaster database");
            throw new RuntimeException(e);
        }

        getCommand("ideas").setExecutor(new OpenIdeasGuiCommand(api, this));
        getCommand("idea").setExecutor(new SendIdeaCommand(ideaCooldownManager, this));

        getCommand("feedback").setExecutor(new FeedbackCommand(this));

        getCommand("bugs").setExecutor(new OpenBugsGuiCommand(api, this));
        getCommand("bug").setExecutor(new SendBugCommand(bugsCooldownManager, this));
    }

    @Override
    public void onDisable() {
        data.closeConnection();
    }
}

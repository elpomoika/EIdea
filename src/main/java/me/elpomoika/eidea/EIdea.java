package me.elpomoika.eidea;

import me.elpomoika.eidea.commands.EideaCommand;
import me.elpomoika.eidea.commands.OpenGuiCommand;
import me.elpomoika.eidea.commands.SendIdeaCommand;
import me.elpomoika.eidea.database.DatabaseConnection;
import me.elpomoika.eidea.database.factories.DatabaseFactory;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.database.mysql.MysqlService;
import me.elpomoika.eidea.models.IdeaStatus;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.models.config.ConfigModel;
import me.elpomoika.eidea.util.CooldownManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.elpomoika.inventoryapi.InventoryApi;

import java.sql.SQLException;

public final class EIdea extends JavaPlugin {
    private CooldownManager manager;
    private InventoryApi api;
    private DatabaseConnection data;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        DatabaseFactory databaseFactory = new DatabaseFactory(this, new BukkitConfigProvider(this));
        this.manager = new CooldownManager();
        IdeaStatus.loadFromConfig(this);

        try {
            data = databaseFactory.getDatabaseConnection(getConfig().getString("database.type"));
            data.getConnection();
            data.createTable();

            this.api = new InventoryApi(this);
        } catch (SQLException e) {
            getLogger().warning("ERROR IN EIDEA database");
            throw new RuntimeException(e);
        }

        getCommand("ideas").setExecutor(new OpenGuiCommand(api, this));
        getCommand("idea").setExecutor(new SendIdeaCommand(manager, this));
        getCommand("eidea").setExecutor(new EideaCommand(this));
    }

    @Override
    public void onDisable() {
        data.closeConnection();
    }
}

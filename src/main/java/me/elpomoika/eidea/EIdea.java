package me.elpomoika.eidea;

import me.elpomoika.eidea.commands.OpenGuiCommand;
import me.elpomoika.eidea.commands.SendIdeaCommand;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.database.mysql.MysqlService;
import me.elpomoika.eidea.models.ConfigModel;
import me.elpomoika.eidea.util.CooldownManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.elpomoika.inventoryapi.InventoryApi;

import java.sql.SQLException;

public final class EIdea extends JavaPlugin {
    private MysqlService service;
    private CooldownManager manager;
    private MysqlRepository repository;
    private InventoryApi api;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        this.service = new MysqlService(new ConfigModel(
                getConfig().getString("mysql.host"),
                getConfig().getString("mysql.port"),
                getConfig().getString("mysql.database"),
                getConfig().getString("mysql.username"),
                getConfig().getString("mysql.password")), this);

        this.manager = new CooldownManager();

        try {
            service.getConnecion();
            service.createTable();

            this.repository = new MysqlRepository(service, manager, this);
            this.api = new InventoryApi(this);
        } catch (SQLException e) {
            getLogger().warning("ERROR IN EIDEA database");
            throw new RuntimeException(e);
        }

        getCommand("ideas").setExecutor(new OpenGuiCommand(repository, api, this));
        getCommand("idea").setExecutor(new SendIdeaCommand(repository, manager, this));
    }

    @Override
    public void onDisable() {
        service.closeConnection();
    }
}

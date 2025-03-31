package me.elpomoika.eidea;

import me.elpomoika.eidea.commands.OpenIdeaGUICommand;
import me.elpomoika.eidea.commands.SendIdeaCommand;
import me.elpomoika.eidea.commands.TestOpenGUI;
import me.elpomoika.eidea.database.sqlite.MysqlRepository;
import me.elpomoika.eidea.database.sqlite.MysqlService;
import me.elpomoika.eidea.listeners.GUIListener;
import me.elpomoika.eidea.models.ConfigModel;
import me.elpomoika.eidea.util.CooldownManager;
import me.elpomoika.eidea.util.future.PendingMenu;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class EIdea extends JavaPlugin {
    private MysqlService service = new MysqlService(new ConfigModel(
            getConfig().getString("mysql.host"),
            getConfig().getString("mysql.port"),
            getConfig().getString("mysql.database"),
            getConfig().getString("mysql.username"),
            getConfig().getString("mysql.password")), this);
    private CooldownManager manager;
    private MysqlRepository repository;

    @Override
    public void onEnable() {
        this.manager = new CooldownManager();
        this.repository = new MysqlRepository(service, manager, this);
        saveDefaultConfig();
        reloadConfig();

        try {
            service.getConnecion();
            service.createTable();
        } catch (SQLException e) {
            getLogger().warning("ERROR IN EIDEA database");
            throw new RuntimeException(e);
        }
        getCommand("ideas").setExecutor(new OpenIdeaGUICommand(repository, this));
        getCommand("idea-test").setExecutor(new TestOpenGUI(repository));
        getCommand("idea").setExecutor(new SendIdeaCommand(repository, manager, this));
        getServer().getPluginManager().registerEvents(new GUIListener(repository, this), this);
    }

    @Override
    public void onDisable() {
        service.closeConnection();
    }
}

package me.elpomoika.eidea.database.factories;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.DatabaseConnection;
import me.elpomoika.eidea.database.mysql.MysqlService;
import me.elpomoika.eidea.database.sqlite.SqliteService;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.models.config.ConfigModel;

public class DatabaseFactory {
    private final EIdea plugin;
    private final BukkitConfigProvider config;

    public DatabaseFactory(EIdea plugin, BukkitConfigProvider config) {
        this.plugin = plugin;
        this.config = config;
    }

    public DatabaseConnection getDatabaseConnection(String dbType) {
        return switch (dbType.toLowerCase()) {
            case ("sqlite") -> new SqliteService(plugin);
            case ("mysql") -> new MysqlService(new ConfigModel(
                    config.getHost(),
                    config.getPort(),
                    config.getDatabase(),
                    config.getUsername(),
                    config.getPassword()), plugin);
            default -> throw new IllegalArgumentException("Incorrect database type " + dbType);
        };
    }
}

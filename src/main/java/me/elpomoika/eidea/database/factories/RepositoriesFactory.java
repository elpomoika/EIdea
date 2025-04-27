package me.elpomoika.eidea.database.factories;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.database.mysql.MysqlService;
import me.elpomoika.eidea.database.sqlite.SqliteRepository;
import me.elpomoika.eidea.database.sqlite.SqliteService;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.models.config.ConfigModel;
import me.elpomoika.eidea.util.CooldownManager;

public class RepositoriesFactory {

    private final EIdea plugin;
    private final BukkitConfigProvider provider;
    private CooldownManager cooldownManager;

    public RepositoriesFactory(EIdea plugin, BukkitConfigProvider provider) {
        this.plugin = plugin;
        this.provider = provider;
    }

    public Repository getRepository(String dbType) {
        cooldownManager = new CooldownManager();
        return switch (dbType.toLowerCase()) {
            case ("sqlite") ->
                    new SqliteRepository(new SqliteService(plugin), cooldownManager ,plugin);
            case ("mysql") ->
                    new MysqlRepository(new MysqlService(new ConfigModel(provider.getHost(),
                            provider.getPort(),
                            provider.getDatabase(),
                            provider.getUsername(),
                            provider.getPassword()), plugin), cooldownManager, plugin);
            default -> throw new IllegalArgumentException("Incorrect database type " + dbType);
        };
    }
}

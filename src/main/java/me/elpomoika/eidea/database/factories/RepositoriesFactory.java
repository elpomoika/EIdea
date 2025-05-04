package me.elpomoika.eidea.database.factories;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.Repository;
import me.elpomoika.eidea.database.mysql.MysqlRepository;
import me.elpomoika.eidea.database.mysql.MysqlService;
import me.elpomoika.eidea.database.sqlite.SqliteRepository;
import me.elpomoika.eidea.database.sqlite.SqliteService;
import me.elpomoika.eidea.models.config.BukkitConfigProvider;
import me.elpomoika.eidea.models.config.ConfigModel;

public class RepositoriesFactory {

    private final FeedbackMaster plugin;
    private final BukkitConfigProvider provider;

    public RepositoriesFactory(FeedbackMaster plugin, BukkitConfigProvider provider) {
        this.plugin = plugin;
        this.provider = provider;
    }

    public Repository getRepository(String dbType) {
        return switch (dbType.toLowerCase()) {
            case ("sqlite") ->
                    new SqliteRepository(new SqliteService(plugin), plugin);
            case ("mysql") ->
                    new MysqlRepository(new MysqlService(new ConfigModel(provider.getHost(),
                            provider.getPort(),
                            provider.getDatabase(),
                            provider.getUsername(),
                            provider.getPassword()), plugin), plugin);
            default -> throw new IllegalArgumentException("Incorrect database type " + dbType);
        };
    }
}

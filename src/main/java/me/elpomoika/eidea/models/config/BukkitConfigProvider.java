package me.elpomoika.eidea.models.config;

import me.elpomoika.eidea.FeedbackMaster;

public class BukkitConfigProvider implements ConfigProvider{
    private final FeedbackMaster plugin;

    public BukkitConfigProvider(FeedbackMaster plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getHost() {
        return plugin.getConfig().getString("database.host");
    }

    @Override
    public String getPort() {
        return plugin.getConfig().getString("database.port");
    }

    @Override
    public String getDatabase() {
        return plugin.getConfig().getString("database.database");
    }

    @Override
    public String getUsername() {
        return plugin.getConfig().getString("database.username");
    }

    @Override
    public String getPassword() {
        return plugin.getConfig().getString("database.password");
    }
}

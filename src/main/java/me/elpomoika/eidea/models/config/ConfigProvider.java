package me.elpomoika.eidea.models.config;

public interface ConfigProvider {
    String getHost();
    String getPort();
    String getDatabase();
    String getUsername();
    String getPassword();
}
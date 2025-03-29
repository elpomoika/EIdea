package me.elpomoika.eidea.models;

import lombok.Getter;

@Getter
public class ConfigModel {
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public ConfigModel(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }


    public String getJdbcUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }
}

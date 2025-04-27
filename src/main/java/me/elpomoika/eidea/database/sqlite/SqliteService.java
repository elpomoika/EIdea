package me.elpomoika.eidea.database.sqlite;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteService implements DatabaseConnection {
    private Connection connection;
    private String path;
    private String url;
    private final EIdea plugin;

    public SqliteService(EIdea plugin) {
        this.plugin = plugin;
    }


    @Override
    public Connection getConnection() {
        path = plugin.getDataFolder().getAbsolutePath() + "/eidea.db";
        url = "jdbc:sqlite:" + path;
        try {
            connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to sqlite database", e);
        }
    }

    @Override
    public void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER PRIMARY KEY, " +
                    "uuid TEXT NOT NULL, " +
                    "idea TEXT NOT NULL, " +
                    "status VARCHAR(10) NOT NULL DEFAULT 'В ОЖИДАНИИ')");
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

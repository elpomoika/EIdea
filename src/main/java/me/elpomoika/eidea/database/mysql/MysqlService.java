package me.elpomoika.eidea.database.mysql;

import me.elpomoika.eidea.EIdea;
import me.elpomoika.eidea.database.DatabaseConnection;
import me.elpomoika.eidea.models.config.ConfigModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlService implements DatabaseConnection {
    private Connection connection;
    private final ConfigModel config;
    private final EIdea plugin;

    public MysqlService(ConfigModel config, EIdea plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    @Override
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword());
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "uuid TEXT NOT NULL, " +
                    "idea TEXT NOT NULL, " +
                    "status TINYINT NOT NULL DEFAULT 0)");
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (!connection.isClosed() && connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package me.elpomoika.eidea.database.mysql;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.DatabaseConnection;
import me.elpomoika.eidea.models.config.ConfigModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlService implements DatabaseConnection {
    private Connection connection;
    private final ConfigModel config;
    private final FeedbackMaster plugin;

    public MysqlService(ConfigModel config, FeedbackMaster plugin) {
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

    // todo make feedback type
    @Override
    public void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "idea TEXT NOT NULL," +
                    "type TINYINT NOT NULL," +
                    "status TINYINT NOT NULL DEFAULT 0)" +
                    "CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci");
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

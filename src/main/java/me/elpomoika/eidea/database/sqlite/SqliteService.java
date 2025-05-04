package me.elpomoika.eidea.database.sqlite;

import me.elpomoika.eidea.FeedbackMaster;
import me.elpomoika.eidea.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteService implements DatabaseConnection {
    private Connection connection;
    private String path;
    private String url;
    private final FeedbackMaster plugin;

    public SqliteService(FeedbackMaster plugin) {
        this.plugin = plugin;
    }


    @Override
    public Connection getConnection() {
        path = plugin.getDataFolder().getAbsolutePath() + "/feedback.db";
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
                    "idea TEXT NOT NULL," +
                    "type TINYINT NOT NULL, " +
                    "status TINYINT NOT NULL DEFAULT 0)");
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

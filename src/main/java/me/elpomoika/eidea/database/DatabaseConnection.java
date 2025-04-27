package me.elpomoika.eidea.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnection {
    Connection getConnection();
    void createTable() throws SQLException;
    void closeConnection();
}

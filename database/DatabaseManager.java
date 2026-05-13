package database;

import java.sql.*;
import java.io.File;

public class DatabaseManager {
    private static final String DB_PATH = System.getProperty("user.home") + File.separator + "Gaming-Hub" + File.separator + "gaming_hub.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static Connection connection;

    public static void initialize() {
        try {
            // Create directory if it doesn't exist
            File dbDir = new File(System.getProperty("user.home") + File.separator + "Gaming-Hub");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
            createTables();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "total_play_time INTEGER DEFAULT 0," +
                    "total_score INTEGER DEFAULT 0," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Game scores table
            stmt.execute("CREATE TABLE IF NOT EXISTS game_scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "game_name TEXT NOT NULL," +
                    "score INTEGER NOT NULL," +
                    "play_time INTEGER NOT NULL," +
                    "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE)");
        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}

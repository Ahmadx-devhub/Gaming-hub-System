package database;

import models.User;
import java.sql.*;

public class UserDAO {

    private static Connection conn() {
        return DatabaseManager.getConnection();
    }

    /**
     * Case-insensitive username match, trimmed, so login works regardless of capitalization.
     */
    public static User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        Connection c = conn();
        if (c == null) {
            System.err.println("getUserByUsername: no database connection");
            return null;
        }
        String key = username.trim().toLowerCase();
        String sql = "SELECT * FROM users WHERE LOWER(TRIM(username)) = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, key);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getLong("total_play_time"),
                            rs.getInt("total_score")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserById(int id) {
        Connection c = conn();
        if (c == null) {
            return null;
        }
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getLong("total_play_time"),
                            rs.getInt("total_score")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createUser(String username, String password) {
        Connection c = conn();
        if (c == null) {
            return false;
        }
        String u = username == null ? "" : username.trim();
        if (u.isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO users (username, password, total_play_time, total_score) VALUES (?, ?, 0, 0)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, u);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateUserStats(int userId, long totalPlayTime, int totalScore) {
        Connection c = conn();
        if (c == null) {
            return false;
        }
        String sql = "UPDATE users SET total_play_time = ?, total_score = ? WHERE id = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setLong(1, totalPlayTime);
            pstmt.setInt(2, totalScore);
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating user stats: " + e.getMessage());
            return false;
        }
    }

    public static boolean userExists(String username) {
        return getUserByUsername(username) != null;
    }
}

package database;

import models.GameScore;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameScoreDAO {

    private static Connection conn() {
        return DatabaseManager.getConnection();
    }

    public static boolean saveGameScore(GameScore score) {
        Connection c = conn();
        if (c == null) {
            return false;
        }
        String sql = "INSERT INTO game_scores (user_id, game_name, score, play_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, score.getUserId());
            pstmt.setString(2, score.getGameName());
            pstmt.setInt(3, score.getScore());
            pstmt.setLong(4, score.getPlayTime());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error saving game score: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<GameScore> getScoresByUser(int userId) {
        List<GameScore> scores = new ArrayList<>();
        Connection c = conn();
        if (c == null) {
            return scores;
        }
        String sql = "SELECT * FROM game_scores WHERE user_id = ? ORDER BY played_at DESC";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("played_at");
                    scores.add(new GameScore(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("game_name"),
                            rs.getInt("score"),
                            rs.getLong("play_time"),
                            ts != null ? ts.toLocalDateTime() : LocalDateTime.now()
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching scores: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }

    public static List<GameScore> getScoresByGame(String gameName) {
        List<GameScore> scores = new ArrayList<>();
        Connection c = conn();
        if (c == null) {
            return scores;
        }
        String sql = "SELECT * FROM game_scores WHERE game_name = ? ORDER BY score DESC LIMIT 10";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("played_at");
                    scores.add(new GameScore(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("game_name"),
                            rs.getInt("score"),
                            rs.getLong("play_time"),
                            ts != null ? ts.toLocalDateTime() : LocalDateTime.now()
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching game scores: " + e.getMessage());
            e.printStackTrace();
        }
        return scores;
    }
}

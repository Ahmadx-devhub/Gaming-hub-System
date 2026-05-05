package database;

import models.GameScore;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameScoreDAO {

    public static boolean saveGameScore(GameScore score) {
        String sql = "INSERT INTO game_scores (user_id, game_name, score, play_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, score.getUserId());
            pstmt.setString(2, score.getGameName());
            pstmt.setInt(3, score.getScore());
            pstmt.setLong(4, score.getPlayTime());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error saving game score: " + e.getMessage());
            return false;
        }
    }

    public static List<GameScore> getScoresByUser(int userId) {
        String sql = "SELECT * FROM game_scores WHERE user_id = ? ORDER BY played_at DESC";
        List<GameScore> scores = new ArrayList<>();
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                scores.add(new GameScore(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("game_name"),
                        rs.getInt("score"),
                        rs.getLong("play_time"),
                        rs.getTimestamp("played_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching scores: " + e.getMessage());
        }
        return scores;
    }

    public static List<GameScore> getScoresByGame(String gameName) {
        String sql = "SELECT * FROM game_scores WHERE game_name = ? ORDER BY score DESC LIMIT 10";
        List<GameScore> scores = new ArrayList<>();
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                scores.add(new GameScore(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("game_name"),
                        rs.getInt("score"),
                        rs.getLong("play_time"),
                        rs.getTimestamp("played_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching game scores: " + e.getMessage());
        }
        return scores;
    }
}

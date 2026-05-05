package models;

import java.time.LocalDateTime;

public class GameScore {
    private int id;
    private int userId;
    private String gameName;
    private int score;
    private long playTime; // in milliseconds
    private LocalDateTime playedAt;

    public GameScore(int userId, String gameName, int score, long playTime) {
        this.userId = userId;
        this.gameName = gameName;
        this.score = score;
        this.playTime = playTime;
        this.playedAt = LocalDateTime.now();
    }

    public GameScore(int id, int userId, String gameName, int score, long playTime, LocalDateTime playedAt) {
        this.id = id;
        this.userId = userId;
        this.gameName = gameName;
        this.score = score;
        this.playTime = playTime;
        this.playedAt = playedAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getGameName() { return gameName; }
    public int getScore() { return score; }
    public long getPlayTime() { return playTime; }
    public LocalDateTime getPlayedAt() { return playedAt; }
}

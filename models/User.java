package models;

public class User {
    private int id;
    private String username;
    private String password;
    private long totalPlayTime; // in milliseconds
    private int totalScore;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.totalPlayTime = 0;
        this.totalScore = 0;
    }

    public User(int id, String username, String password, long totalPlayTime, int totalScore) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.totalPlayTime = totalPlayTime;
        this.totalScore = totalScore;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public long getTotalPlayTime() { return totalPlayTime; }
    public int getTotalScore() { return totalScore; }

    public void setTotalPlayTime(long time) { this.totalPlayTime = time; }
    public void setTotalScore(int score) { this.totalScore = score; }
}
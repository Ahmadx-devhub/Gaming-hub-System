package gui;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import models.User;
import services.AuthenticationService;
import database.UserDAO;
import database.GameScoreDAO;
import models.GameScore;
import games.TicTacToeGame;
import games.SnakeGame;
import games.NumberGuessingGame;
import java.util.List;

public class Dashboard extends JFrame {
    private JLabel welcomeLabel;
    private JLabel totalScoreLabel;
    private JLabel totalPlayTimeLabel;
    private JPanel gamesPanel;
    private User currentUser;

    public Dashboard() {
        setTitle("Gaming Hub - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        currentUser = AuthenticationService.getCurrentUser();

        // Main panel with soft background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 247)); // Soft light background
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel - User info and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        infoPanel.setOpaque(false);

        welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(40, 40, 45)); // Dark text
        infoPanel.add(welcomeLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(220, 100, 100)); // Soft red
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> handleLogout());
        infoPanel.add(logoutButton);

        topPanel.add(infoPanel, BorderLayout.WEST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        statsPanel.setBackground(new Color(235, 240, 245)); // Very soft blue background
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        totalScoreLabel = new JLabel("Total Score: " + currentUser.getTotalScore());
        totalScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalScoreLabel.setForeground(new Color(80, 120, 180)); // Soft blue
        statsPanel.add(totalScoreLabel);

        long hours = currentUser.getTotalPlayTime() / 3600000;
        long minutes = (currentUser.getTotalPlayTime() % 3600000) / 60000;
        totalPlayTimeLabel = new JLabel("Total Play Time: " + hours + "h " + minutes + "m");
        totalPlayTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPlayTimeLabel.setForeground(new Color(100, 160, 100)); // Soft green
        statsPanel.add(totalPlayTimeLabel);

        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        // Games panel
        JLabel gamesTitle = new JLabel("Select a Game:");
        gamesTitle.setFont(new Font("Arial", Font.BOLD, 20));
        gamesTitle.setForeground(new Color(40, 40, 45)); // Dark text

        gamesPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        gamesPanel.setOpaque(false);

        addGameButton("Tic Tac Toe", new Color(200, 120, 120), "TicTacToe");
        addGameButton("Snake", new Color(120, 180, 120), "Snake");
        addGameButton("Number Guessing", new Color(120, 150, 200), "NumberGuessing");

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(gamesTitle, BorderLayout.NORTH);
        centerPanel.add(gamesPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void addGameButton(String gameName, Color backgroundColor, String gameClass) {
        JButton gameButton = new JButton(gameName) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };

        gameButton.setFont(new Font("Arial", Font.BOLD, 16));
        gameButton.setForeground(Color.WHITE);
        gameButton.setFocusPainted(false);
        gameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gameButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gameButton.setContentAreaFilled(false);
        gameButton.setOpaque(false);

        gameButton.addActionListener(e -> startGame(gameClass));
        gamesPanel.add(gameButton);
    }

    private void startGame(String gameClass) {
        dispose();
        switch (gameClass) {
            case "TicTacToe":
                new TicTacToeGame();
                break;
            case "Snake":
                new SnakeGame();
                break;
            case "NumberGuessing":
                new NumberGuessingGame();
                break;
        }
    }

    private void handleLogout() {
        AuthenticationService.logout();
        dispose();
        new LoginPage();
    }

    public void updateStats() {
        currentUser = UserDAO.getUserById(currentUser.getId());
        if (currentUser != null) {
            totalScoreLabel.setText("Total Score: " + currentUser.getTotalScore());
            long hours = currentUser.getTotalPlayTime() / 3600000;
            long minutes = (currentUser.getTotalPlayTime() % 3600000) / 60000;
            totalPlayTimeLabel.setText("Total Play Time: " + hours + "h " + minutes + "m");
        }
    }
}

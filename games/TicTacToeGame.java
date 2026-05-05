package games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import services.AuthenticationService;
import models.GameScore;
import database.GameScoreDAO;
import database.UserDAO;
import gui.Dashboard;

public class TicTacToeGame extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean isXNext = true;
    private int score = 0;
    private long startTime;
    private JLabel scoreLabel;
    private JButton resetButton;
    private JButton backButton;

    public TicTacToeGame() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        startTime = System.currentTimeMillis();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 247)); // Soft light

        // Top panel with title
        JLabel titleLabel = new JLabel("Tic Tac Toe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(40, 40, 45)); // Dark text
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Game board
        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        boardPanel.setBackground(new Color(220, 220, 225)); // Soft gray
        boardPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setBackground(new Color(255, 255, 255)); // White
                buttons[i][j].setForeground(new Color(40, 80, 140)); // Dark blue
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setCursor(new Cursor(Cursor.HAND_CURSOR));
                buttons[i][j].setBorder(BorderFactory.createLineBorder(new Color(180, 180, 185), 2));

                int row = i;
                int col = j;
                buttons[i][j].addActionListener(e -> makeMove(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }

        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(235, 240, 245)); // Very soft blue
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scoreLabel = new JLabel("Score: " + score + " | Player: X");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(60, 60, 65)); // Dark gray
        bottomPanel.add(scoreLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        resetButton = new JButton("New Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 12));
        resetButton.setBackground(new Color(120, 180, 120)); // Soft green
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        resetButton.addActionListener(e -> resetGame());
        buttonPanel.add(resetButton);

        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(220, 100, 100)); // Soft red
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        backButton.addActionListener(e -> goBack());
        buttonPanel.add(backButton);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void makeMove(int row, int col) {
        if (!buttons[row][col].getText().isEmpty()) {
            return;
        }

        buttons[row][col].setText(isXNext ? "X" : "O");
        buttons[row][col].setEnabled(false);

        if (checkWin(isXNext ? "X" : "O")) {
            score += 10;
            JOptionPane.showMessageDialog(this, (isXNext ? "X" : "O") + " wins! +10 points");
            saveScore();
            goBack();
            return;
        }

        if (checkDraw()) {
            score += 5;
            JOptionPane.showMessageDialog(this, "Draw! +5 points");
            saveScore();
            goBack();
            return;
        }

        isXNext = !isXNext;
        scoreLabel.setText("Score: " + score + " | Player: " + (isXNext ? "X" : "O"));
    }

    private boolean checkWin(String player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(player) &&
                    buttons[i][1].getText().equals(player) &&
                    buttons[i][2].getText().equals(player)) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (buttons[0][j].getText().equals(player) &&
                    buttons[1][j].getText().equals(player) &&
                    buttons[2][j].getText().equals(player)) {
                return true;
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][2].getText().equals(player)) {
            return true;
        }

        if (buttons[0][2].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][0].getText().equals(player)) {
            return true;
        }

        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        isXNext = true;
        scoreLabel.setText("Score: " + score + " | Player: X");
    }

    private void saveScore() {
        long playTime = System.currentTimeMillis() - startTime;
        GameScore gameScore = new GameScore(
                AuthenticationService.getCurrentUser().getId(),
                "TicTacToe",
                score,
                playTime
        );
        GameScoreDAO.saveGameScore(gameScore);

        // Update user stats
        int userId = AuthenticationService.getCurrentUser().getId();
        int newTotalScore = AuthenticationService.getCurrentUser().getTotalScore() + score;
        long newTotalPlayTime = AuthenticationService.getCurrentUser().getTotalPlayTime() + playTime;
        UserDAO.updateUserStats(userId, newTotalPlayTime, newTotalScore);
    }

    private void goBack() {
        dispose();
        Dashboard dashboard = new Dashboard();
        dashboard.updateStats();
    }
}

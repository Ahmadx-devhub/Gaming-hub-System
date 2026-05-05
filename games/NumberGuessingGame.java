package games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import services.AuthenticationService;
import models.GameScore;
import database.GameScoreDAO;
import database.UserDAO;
import gui.Dashboard;

public class NumberGuessingGame extends JFrame {
    private int secretNumber;
    private int attempts = 0;
    private int score = 0;
    private long startTime;
    private JLabel messageLabel;
    private JTextField guessField;
    private JButton submitButton;
    private JButton backButton;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private boolean gameOver = false;
    private final int MAX_ATTEMPTS = 10;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        startTime = System.currentTimeMillis();
        secretNumber = new Random().nextInt(100) + 1;

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 245, 247)); // Soft light background
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Number Guessing Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(40, 40, 45)); // Dark text
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Instructions
        JLabel instrLabel = new JLabel("Guess a number between 1 and 100");
        instrLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instrLabel.setForeground(new Color(100, 100, 105)); // Medium gray
        gbc.gridy = 1;
        mainPanel.add(instrLabel, gbc);

        // Score and attempts
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(120, 150, 180)); // Soft blue
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(scoreLabel, gbc);

        attemptsLabel = new JLabel("Attempts: " + attempts + "/" + MAX_ATTEMPTS);
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        attemptsLabel.setForeground(new Color(200, 120, 120)); // Soft red
        gbc.gridx = 1;
        mainPanel.add(attemptsLabel, gbc);

        // Message label
        messageLabel = new JLabel("Enter your guess");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(new Color(60, 60, 65)); // Dark gray
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(messageLabel, gbc);

        // Input field
        guessField = new JTextField(10);
        guessField.setFont(new Font("Arial", Font.PLAIN, 18));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setBackground(new Color(255, 255, 255)); // White
        guessField.setForeground(new Color(30, 30, 35)); // Dark
        guessField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 205), 1));
        guessField.addActionListener(this::handleGuess);
        gbc.gridy = 4;
        mainPanel.add(guessField, gbc);

        // Submit button
        submitButton = new JButton("Submit Guess");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(120, 180, 120)); // Soft green
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(this::handleGuess);
        gbc.gridy = 5;
        mainPanel.add(submitButton, gbc);

        // Back button
        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(220, 100, 100)); // Soft red
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> goBack());
        gbc.gridy = 6;
        mainPanel.add(backButton, gbc);

        add(mainPanel);
        setVisible(true);
        guessField.requestFocus();
    }

    private void handleGuess(ActionEvent e) {
        if (gameOver) return;

        try {
            int guess = Integer.parseInt(guessField.getText().trim());
            guessField.setText("");

            if (guess < 1 || guess > 100) {
                messageLabel.setText("Please enter a number between 1 and 100");
                messageLabel.setForeground(new Color(200, 100, 100)); // Soft red
                return;
            }

            attempts++;
            attemptsLabel.setText("Attempts: " + attempts + "/" + MAX_ATTEMPTS);

            if (guess == secretNumber) {
                score = Math.max(0, 100 - (attempts * 10));
                messageLabel.setText("Correct! You won! Score: " + score);
                messageLabel.setForeground(new Color(100, 150, 100)); // Soft green
                scoreLabel.setText("Score: " + score);
                gameOver = true;
                submitButton.setEnabled(false);
                guessField.setEnabled(false);
                saveScoreAndExit();
            } else if (guess < secretNumber) {
                messageLabel.setText("Too low! Try higher");
                messageLabel.setForeground(new Color(180, 150, 100)); // Soft orange
            } else {
                messageLabel.setText("Too high! Try lower");
                messageLabel.setForeground(new Color(180, 150, 100)); // Soft orange
            }

            if (attempts >= MAX_ATTEMPTS && guess != secretNumber) {
                messageLabel.setText("Game Over! The number was: " + secretNumber);
                messageLabel.setForeground(new Color(200, 100, 100)); // Soft red
                score = 0;
                gameOver = true;
                submitButton.setEnabled(false);
                guessField.setEnabled(false);
                saveScoreAndExit();
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("Please enter a valid number");
            messageLabel.setForeground(new Color(200, 100, 100)); // Soft red
        }
    }

    private void saveScoreAndExit() {
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(2000);
                long playTime = System.currentTimeMillis() - startTime;
                GameScore gameScore = new GameScore(
                        AuthenticationService.getCurrentUser().getId(),
                        "NumberGuessing",
                        score,
                        playTime
                );
                GameScoreDAO.saveGameScore(gameScore);

                int userId = AuthenticationService.getCurrentUser().getId();
                int newTotalScore = AuthenticationService.getCurrentUser().getTotalScore() + score;
                long newTotalPlayTime = AuthenticationService.getCurrentUser().getTotalPlayTime() + playTime;
                UserDAO.updateUserStats(userId, newTotalPlayTime, newTotalScore);

                dispose();
                Dashboard dashboard = new Dashboard();
                dashboard.updateStats();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void goBack() {
        dispose();
        Dashboard dashboard = new Dashboard();
        dashboard.updateStats();
    }
}

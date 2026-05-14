package games;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import services.AuthenticationService;
import models.GameScore;
import database.GameScoreDAO;
import database.UserDAO;
import gui.Dashboard;
import gui.GameHubTheme;

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
    private JProgressBar attemptBar;
    private boolean gameOver = false;
    private final int maxAttempts = 10;

    public NumberGuessingGame() {
        setTitle("Gaming Hub — Number Guess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 520));
        setSize(560, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        startTime = System.currentTimeMillis();
        secretNumber = new Random().nextInt(100) + 1;

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                GameHubTheme.paintHubGradient((Graphics2D) g, getWidth(), getHeight());
            }
        };

        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(true);
        card.setBackground(GameHubTheme.BG_CARD);
        card.setBorder(GameHubTheme.neonCardPadding(26, 30, 26, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;

        JPanel titleRow = GameHubTheme.createTwoWordTitle(
                "NUMBER", GameHubTheme.ACCENT_AMBER,
                "GUESS", GameHubTheme.TEXT_PRIMARY,
                22);
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(titleRow, gbc);

        JLabel instrLabel = new JLabel("Pick a number from 1 to 100 — you have " + maxAttempts + " attempts");
        instrLabel.setFont(GameHubTheme.fontSubtitle());
        instrLabel.setForeground(GameHubTheme.TEXT_MUTED);
        gbc.gridy = 1;
        card.add(instrLabel, gbc);

        scoreLabel = new JLabel("Best this run: 0");
        scoreLabel.setFont(GameHubTheme.fontBodyBold());
        scoreLabel.setForeground(GameHubTheme.ACCENT_CYAN);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        card.add(scoreLabel, gbc);

        attemptsLabel = new JLabel("Attempts: 0 / " + maxAttempts);
        attemptsLabel.setFont(GameHubTheme.fontBodyBold());
        attemptsLabel.setForeground(GameHubTheme.ACCENT_AMBER);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        card.add(attemptsLabel, gbc);

        attemptBar = new JProgressBar(0, maxAttempts);
        attemptBar.setValue(0);
        attemptBar.setString("Attempts used");
        GameHubTheme.styleGameProgressBar(attemptBar);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(attemptBar, gbc);

        messageLabel = new JLabel("<html><body style='width:380px'>Type a number and press Enter.</body></html>");
        messageLabel.setFont(GameHubTheme.fontBodyBold());
        messageLabel.setForeground(GameHubTheme.TEXT_PRIMARY);
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridy = 4;
        card.add(messageLabel, gbc);

        guessField = new JTextField(8);
        guessField.setHorizontalAlignment(JTextField.CENTER);
        GameHubTheme.styleTextField(guessField);
        guessField.setFont(GameHubTheme.fontMono());
        guessField.addActionListener(this::handleGuess);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(guessField, gbc);

        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);

        submitButton = GameHubTheme.createPillButton("Submit guess", GameHubTheme.ACCENT_GREEN, new Color(52, 196, 132));
        submitButton.addActionListener(this::handleGuess);
        row.add(submitButton);

        backButton = GameHubTheme.createGhostButton("Back to lobby");
        backButton.addActionListener(e -> goBack());
        row.add(backButton);

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(row, gbc);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(16, 16, 16, 16);
        wrap.add(card, c);

        root.add(wrap, BorderLayout.CENTER);
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        add(root);
        getRootPane().setDefaultButton(submitButton);
        setVisible(true);
        guessField.requestFocusInWindow();
    }

    private void handleGuess(ActionEvent e) {
        if (gameOver) {
            return;
        }

        try {
            int guess = Integer.parseInt(guessField.getText().trim());
            guessField.setText("");

            if (guess < 1 || guess > 100) {
                messageLabel.setForeground(GameHubTheme.TEXT_ERROR);
                messageLabel.setText("Stay within 1 and 100.");
                return;
            }

            attempts++;
            attemptsLabel.setText("Attempts: " + attempts + " / " + maxAttempts);
            attemptBar.setValue(attempts);

            if (guess == secretNumber) {
                score = Math.max(0, 100 - (attempts * 10));
                messageLabel.setForeground(GameHubTheme.TEXT_SUCCESS);
                messageLabel.setText("Nailed it — +" + score + " points");
                scoreLabel.setText("Best this run: " + score);
                finishSession();
            } else if (guess < secretNumber) {
                messageLabel.setForeground(GameHubTheme.ACCENT_AMBER);
                messageLabel.setText("Higher than " + guess);
            } else {
                messageLabel.setForeground(GameHubTheme.ACCENT_AMBER);
                messageLabel.setText("Lower than " + guess);
            }

            if (attempts >= maxAttempts && guess != secretNumber) {
                messageLabel.setForeground(GameHubTheme.TEXT_ERROR);
                messageLabel.setText("Out of tries — secret was " + secretNumber);
                score = 0;
                scoreLabel.setText("Best this run: 0");
                finishSession();
            }
        } catch (NumberFormatException ex) {
            messageLabel.setForeground(GameHubTheme.TEXT_ERROR);
            messageLabel.setText("Numbers only, please.");
        }
    }

    private void finishSession() {
        gameOver = true;
        submitButton.setEnabled(false);
        guessField.setEnabled(false);
        backButton.setEnabled(false);

        Timer exitTimer = new Timer(1800, ev -> {
            ((Timer) ev.getSource()).stop();
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
        });
        exitTimer.setRepeats(false);
        exitTimer.start();
    }

    private void goBack() {
        dispose();
        Dashboard dashboard = new Dashboard();
        dashboard.updateStats();
    }
}

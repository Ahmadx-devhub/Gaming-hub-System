package games;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import services.AuthenticationService;
import models.GameScore;
import database.GameScoreDAO;
import database.UserDAO;
import gui.Dashboard;
import gui.GameHubTheme;

public class TicTacToeGame extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean isXNext = true;
    private int score = 0;
    private long startTime;
    private JLabel scoreLabel;
    private JButton resetButton;
    private JButton backButton;

    public TicTacToeGame() {
        setTitle("Gaming Hub — Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 600));
        setSize(560, 680);
        setLocationRelativeTo(null);
        setResizable(true);

        startTime = System.currentTimeMillis();

        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                GameHubTheme.paintHubGradient((Graphics2D) g, getWidth(), getHeight());
            }
        };

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(20, 20, 12, 20));

        JPanel titleRow = GameHubTheme.createTripleWordTitle(
                "TIC", GameHubTheme.ACCENT_CYAN,
                "TAC", GameHubTheme.ACCENT_MAGENTA,
                "TOE", GameHubTheme.ACCENT_GREEN,
                24);
        JLabel sub = new JLabel("Local hot-seat — X opens");
        sub.setFont(GameHubTheme.fontSubtitle());
        sub.setForeground(GameHubTheme.TEXT_MUTED);

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);
        titles.add(titleRow);
        titles.add(Box.createVerticalStrut(4));
        titles.add(sub);
        top.add(titles, BorderLayout.WEST);

        JPanel boardWrap = new JPanel(new GridBagLayout());
        boardWrap.setOpaque(false);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        boardPanel.setOpaque(false);
        boardPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        Color fill = GameHubTheme.BG_CARD;
                        if (!isEnabled()) {
                            fill = new Color(36, 42, 62);
                        } else if (getModel().isRollover() && getText().isEmpty()) {
                            fill = GameHubTheme.BG_ELEVATED;
                        }
                        g2.setColor(fill);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                        if (isEnabled() && getText().isEmpty() && getModel().isRollover()) {
                            g2.setStroke(new BasicStroke(2f));
                            g2.setColor(new Color(0, 230, 255, 100));
                            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
                        } else {
                            g2.setColor(GameHubTheme.BORDER_LINE);
                            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                        }
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
                buttons[i][j].setForeground(GameHubTheme.ACCENT_CYAN);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                buttons[i][j].setContentAreaFilled(false);
                buttons[i][j].setOpaque(false);
                buttons[i][j].setBorder(new EmptyBorder(4, 4, 4, 4));
                buttons[i][j].setRolloverEnabled(true);

                int row = i;
                int col = j;
                buttons[i][j].addActionListener(e -> makeMove(row, col));

                buttons[i][j].setPreferredSize(new Dimension(88, 88));
                buttons[i][j].setMinimumSize(new Dimension(64, 64));
                boardPanel.add(buttons[i][j]);
            }
        }

        JPanel boardCard = new JPanel(new BorderLayout());
        boardCard.setOpaque(true);
        boardCard.setBackground(GameHubTheme.BG_PANEL);
        boardCard.setBorder(GameHubTheme.neonCardPadding(12, 12, 12, 12));
        boardCard.add(boardPanel, BorderLayout.CENTER);

        boardWrap.add(boardCard);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(12, 20, 20, 20));

        scoreLabel = new JLabel("Round score: 0  ·  Turn: X");
        scoreLabel.setFont(GameHubTheme.fontBodyBold());
        scoreLabel.setForeground(GameHubTheme.TEXT_PRIMARY);
        bottom.add(scoreLabel, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        resetButton = GameHubTheme.createToolbarButton("New round", GameHubTheme.BG_ELEVATED, GameHubTheme.BG_CARD);
        resetButton.addActionListener(e -> resetGame());
        actions.add(resetButton);

        backButton = GameHubTheme.createToolbarButton("Lobby", new Color(90, 40, 120), new Color(120, 50, 160));
        backButton.addActionListener(e -> goBack());
        actions.add(backButton);

        bottom.add(actions, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);
        root.add(boardWrap, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    private void makeMove(int row, int col) {
        if (!buttons[row][col].getText().isEmpty()) {
            return;
        }

        buttons[row][col].setText(isXNext ? "X" : "O");
        buttons[row][col].setForeground(isXNext ? GameHubTheme.ACCENT_CYAN : GameHubTheme.ACCENT_MAGENTA);
        buttons[row][col].setEnabled(false);

        if (checkWin(isXNext ? "X" : "O")) {
            score += 10;
            GameHubTheme.showArcadeDialog(this,
                    "Round won",
                    (isXNext ? "X" : "O") + " takes the round. +10 points.",
                    JOptionPane.INFORMATION_MESSAGE);
            saveScore();
            goBack();
            return;
        }

        if (checkDraw()) {
            score += 5;
            GameHubTheme.showArcadeDialog(this, "Draw", "Stalemate — +5 points.", JOptionPane.INFORMATION_MESSAGE);
            saveScore();
            goBack();
            return;
        }

        isXNext = !isXNext;
        scoreLabel.setText("Round score: " + score + "  ·  Turn: " + (isXNext ? "X" : "O"));
    }

    private boolean checkWin(String player) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(player) &&
                    buttons[i][1].getText().equals(player) &&
                    buttons[i][2].getText().equals(player)) {
                return true;
            }
        }
        for (int j = 0; j < 3; j++) {
            if (buttons[0][j].getText().equals(player) &&
                    buttons[1][j].getText().equals(player) &&
                    buttons[2][j].getText().equals(player)) {
                return true;
            }
        }
        if (buttons[0][0].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][2].getText().equals(player)) {
            return true;
        }
        return buttons[0][2].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][0].getText().equals(player);
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
                buttons[i][j].setForeground(GameHubTheme.ACCENT_CYAN);
                buttons[i][j].setEnabled(true);
            }
        }
        isXNext = true;
        scoreLabel.setText("Round score: " + score + "  ·  Turn: X");
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

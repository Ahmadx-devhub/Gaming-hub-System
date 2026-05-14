package games;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import services.AuthenticationService;
import models.GameScore;
import database.GameScoreDAO;
import database.UserDAO;
import gui.Dashboard;
import gui.GameHubTheme;

public class SnakeGame extends JFrame {
    private GamePanel gamePanel;

    public SnakeGame() {
        setTitle("Gaming Hub — Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 680));
        setSize(680, 760);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel shell = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                GameHubTheme.paintHubGradient((Graphics2D) g, getWidth(), getHeight());
            }
        };

        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.setOpaque(false);
        north.setBorder(new EmptyBorder(16, 20, 8, 20));
        JLabel modeBadge = GameHubTheme.createHudBadge("Arcade");
        modeBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        north.add(modeBadge);
        north.add(Box.createVerticalStrut(8));
        JPanel title = GameHubTheme.createTwoWordTitle(
                "SNAKE", GameHubTheme.ACCENT_GREEN,
                "RUN", GameHubTheme.TEXT_PRIMARY,
                22);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        north.add(title);
        north.add(Box.createVerticalStrut(4));
        JLabel hint = new JLabel("Arrow keys to move. After game over, press Enter to save and return.");
        hint.setFont(GameHubTheme.fontCaption());
        hint.setForeground(GameHubTheme.TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        north.add(hint);

        gamePanel = new GamePanel();
        gamePanel.setBorder(new EmptyBorder(0, 16, 16, 16));

        shell.add(north, BorderLayout.NORTH);
        shell.add(gamePanel, BorderLayout.CENTER);
        add(shell);
        setVisible(true);
        gamePanel.requestFocusInWindow();
    }

    private class GamePanel extends JPanel implements KeyListener {
        private List<Point> snake;
        private Point food;
        private int direction = 1;
        private int nextDirection = 1;
        private int score = 0;
        private boolean gameOver = false;
        private long startTime;
        private final Random random = new Random();
        private final int GRID_SIZE = 20;
        private final int COLS = 28;
        private final int ROWS = 28;

        GamePanel() {
            addKeyListener(this);
            setFocusable(true);
            setOpaque(false);
            setPreferredSize(new Dimension(COLS * GRID_SIZE + 32, ROWS * GRID_SIZE + 88));

            startTime = System.currentTimeMillis();
            snake = new ArrayList<>();
            snake.add(new Point(14, 14));
            snake.add(new Point(13, 14));
            snake.add(new Point(12, 14));

            generateFood();
            new Timer(100, e -> update()).start();
        }

        private int pixelW() {
            return COLS * GRID_SIZE;
        }

        private int pixelH() {
            return ROWS * GRID_SIZE;
        }

        private void generateFood() {
            do {
                food = new Point(random.nextInt(COLS), random.nextInt(ROWS));
            } while (snake.contains(food));
        }

        private void update() {
            if (gameOver) {
                return;
            }

            direction = nextDirection;
            Point head = snake.get(0);
            Point newHead = new Point(head.x, head.y);

            if (direction == 1) {
                newHead.x++;
            } else if (direction == 2) {
                newHead.y++;
            } else if (direction == 3) {
                newHead.x--;
            } else if (direction == 4) {
                newHead.y--;
            }

            if (newHead.x < 0 || newHead.x >= COLS || newHead.y < 0 || newHead.y >= ROWS) {
                endGame();
                return;
            }
            if (snake.contains(newHead)) {
                endGame();
                return;
            }

            snake.add(0, newHead);
            if (newHead.equals(food)) {
                score += 10;
                generateFood();
            } else {
                snake.remove(snake.size() - 1);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ox = (getWidth() - pixelW()) / 2;
            int oy = 8;

            g2d.setColor(GameHubTheme.BG_PANEL);
            g2d.fillRoundRect(ox - 10, oy - 10, pixelW() + 20, pixelH() + 20, 18, 18);
            g2d.setColor(GameHubTheme.BORDER_LINE);
            g2d.drawRoundRect(ox - 10, oy - 10, pixelW() + 20, pixelH() + 20, 18, 18);

            g2d.setColor(new Color(255, 255, 255, 6));
            for (int x = 0; x <= COLS; x++) {
                int px = ox + x * GRID_SIZE;
                g2d.drawLine(px, oy, px, oy + pixelH());
            }
            for (int y = 0; y <= ROWS; y++) {
                int py = oy + y * GRID_SIZE;
                g2d.drawLine(ox, py, ox + pixelW(), py);
            }

            for (int i = 0; i < snake.size(); i++) {
                Point p = snake.get(i);
                int px = ox + p.x * GRID_SIZE;
                int py = oy + p.y * GRID_SIZE;
                if (i == 0) {
                    g2d.setColor(GameHubTheme.ACCENT_GREEN);
                } else {
                    float t = 1f - (float) i / Math.max(8, snake.size());
                    g2d.setColor(new Color(
                            (int) (30 + 80 * t),
                            (int) (180 + 40 * t),
                            (int) (200 - 40 * t)));
                }
                g2d.fillRoundRect(px + 2, py + 2, GRID_SIZE - 4, GRID_SIZE - 4, 8, 8);
            }

            int fx = ox + food.x * GRID_SIZE;
            int fy = oy + food.y * GRID_SIZE;
            g2d.setColor(GameHubTheme.ACCENT_MAGENTA);
            g2d.fillOval(fx + 3, fy + 3, GRID_SIZE - 6, GRID_SIZE - 6);

            int hudY = oy + pixelH() + 18;
            g2d.setColor(GameHubTheme.TEXT_PRIMARY);
            g2d.setFont(GameHubTheme.fontBodyBold());
            g2d.drawString("Score " + score, ox, hudY);

            if (gameOver) {
                g2d.setColor(new Color(10, 12, 22, 170));
                g2d.fillRoundRect(ox, oy, pixelW(), pixelH(), 12, 12);
                g2d.setColor(GameHubTheme.TEXT_PRIMARY);
                g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
                String msg = "Game over";
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(msg, ox + (pixelW() - fm.stringWidth(msg)) / 2, oy + pixelH() / 2 - 8);
                g2d.setFont(GameHubTheme.fontSubtitle());
                String sub = "Score " + score + " — press Enter to return to lobby";
                FontMetrics fm2 = g2d.getFontMetrics();
                g2d.setColor(GameHubTheme.TEXT_MUTED);
                g2d.drawString(sub, ox + (pixelW() - fm2.stringWidth(sub)) / 2, oy + pixelH() / 2 + 22);
            }

            g2d.dispose();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if (direction != 3) {
                        nextDirection = 1;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 4) {
                        nextDirection = 2;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 1) {
                        nextDirection = 3;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 2) {
                        nextDirection = 4;
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (gameOver) {
                        saveScoreAndExit();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        private void endGame() {
            gameOver = true;
            repaint();
        }

        private void saveScoreAndExit() {
            long playTime = System.currentTimeMillis() - startTime;
            GameScore gameScore = new GameScore(
                    AuthenticationService.getCurrentUser().getId(),
                    "Snake",
                    score,
                    playTime
            );
            GameScoreDAO.saveGameScore(gameScore);

            int userId = AuthenticationService.getCurrentUser().getId();
            int newTotalScore = AuthenticationService.getCurrentUser().getTotalScore() + score;
            long newTotalPlayTime = AuthenticationService.getCurrentUser().getTotalPlayTime() + playTime;
            UserDAO.updateUserStats(userId, newTotalPlayTime, newTotalScore);

            SwingUtilities.getWindowAncestor(this).dispose();
            Dashboard dashboard = new Dashboard();
            dashboard.updateStats();
        }
    }
}

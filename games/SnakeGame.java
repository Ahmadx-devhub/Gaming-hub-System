package games;

import javax.swing.*;
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

public class SnakeGame extends JFrame {
    private GamePanel gamePanel;

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);
        setVisible(true);
    }

    private class GamePanel extends JPanel implements KeyListener {
        private List<Point> snake;
        private Point food;
        private int direction = 1; // 1: right, 2: down, 3: left, 4: up
        private int nextDirection = 1;
        private int score = 0;
        private boolean gameOver = false;
        private long startTime;
        private Random random = new Random();
        private final int GRID_SIZE = 20;
        private final int PANEL_WIDTH = 600;
        private final int PANEL_HEIGHT = 600;

        public GamePanel() {
            addKeyListener(this);
            setFocusable(true);
            setBackground(new Color(245, 245, 247)); // Soft light background

            startTime = System.currentTimeMillis();
            snake = new ArrayList<>();
            snake.add(new Point(10, 10));
            snake.add(new Point(9, 10));
            snake.add(new Point(8, 10));

            generateFood();

            new Timer(100, e -> update()).start();
        }

        private void generateFood() {
            do {
                food = new Point(random.nextInt(PANEL_WIDTH / GRID_SIZE),
                        random.nextInt(PANEL_HEIGHT / GRID_SIZE));
            } while (snake.contains(food));
        }

        private void update() {
            if (gameOver) return;

            direction = nextDirection;

            Point head = snake.get(0);
            Point newHead = new Point(head.x, head.y);

            if (direction == 1) newHead.x++;  // right
            else if (direction == 2) newHead.y++; // down
            else if (direction == 3) newHead.x--; // left
            else if (direction == 4) newHead.y--; // up

            // Check collision with walls
            if (newHead.x < 0 || newHead.x >= PANEL_WIDTH / GRID_SIZE ||
                    newHead.y < 0 || newHead.y >= PANEL_HEIGHT / GRID_SIZE) {
                endGame();
                return;
            }

            // Check collision with itself
            if (snake.contains(newHead)) {
                endGame();
                return;
            }

            snake.add(0, newHead);

            // Check if food is eaten
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
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw snake
            g2d.setColor(new Color(100, 140, 100)); // Soft green for body
            for (int i = 0; i < snake.size(); i++) {
                Point p = snake.get(i);
                if (i == 0) {
                    g2d.setColor(new Color(60, 120, 60)); // Darker green for head
                } else {
                    g2d.setColor(new Color(120, 160, 120)); // Lighter green for body
                }
                g2d.fillRect(p.x * GRID_SIZE, p.y * GRID_SIZE, GRID_SIZE - 2, GRID_SIZE - 2);
            }

            // Draw food
            g2d.setColor(new Color(220, 120, 100)); // Soft orange/red
            g2d.fillOval(food.x * GRID_SIZE + 2, food.y * GRID_SIZE + 2, GRID_SIZE - 4, GRID_SIZE - 4);

            // Draw info panel at bottom
            g2d.setColor(new Color(235, 240, 245)); // Very soft blue
            g2d.fillRect(0, PANEL_HEIGHT, PANEL_WIDTH, 50);

            g2d.setColor(new Color(60, 60, 65)); // Dark gray text
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("Score: " + score, 20, PANEL_HEIGHT + 30);

            if (gameOver) {
                g2d.setColor(new Color(220, 100, 100)); // Soft red
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                g2d.drawString("GAME OVER! Score: " + score, PANEL_WIDTH / 2 - 150, PANEL_HEIGHT / 2);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString("Press ENTER to go back", PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2 + 40);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if (direction != 3) nextDirection = 1;
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 4) nextDirection = 2;
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 1) nextDirection = 3;
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 2) nextDirection = 4;
                    break;
                case KeyEvent.VK_ENTER:
                    if (gameOver) {
                        saveScoreAndExit();
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}

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

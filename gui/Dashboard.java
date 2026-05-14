package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import models.User;
import services.AuthenticationService;
import database.UserDAO;
import games.TicTacToeGame;
import games.SnakeGame;
import games.NumberGuessingGame;

public class Dashboard extends JFrame {
    private JLabel welcomeLabel;
    private JLabel totalScoreLabel;
    private JLabel totalPlayTimeLabel;
    private JPanel gamesPanel;
    private User currentUser;

    public Dashboard() {
        setTitle("Gaming Hub — Lobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 640));
        setSize(1080, 760);
        setLocationRelativeTo(null);
        setResizable(true);

        currentUser = AuthenticationService.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null,
                    "No active player session. Please sign in again.",
                    "Gaming Hub",
                    JOptionPane.WARNING_MESSAGE);
            SwingUtilities.invokeLater(LoginPage::new);
            dispose();
            return;
        }

        JPanel root = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                GameHubTheme.paintHubGradient((Graphics2D) g, getWidth(), getHeight());
            }
        };

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(8, 28, 8, 28));

        JPanel leftHead = new JPanel();
        leftHead.setLayout(new BoxLayout(leftHead, BoxLayout.Y_AXIS));
        leftHead.setOpaque(false);

        JLabel kicker = new JLabel("Main menu");
        kicker.setFont(GameHubTheme.fontCaption());
        kicker.setForeground(GameHubTheme.ACCENT_CYAN);
        leftHead.add(kicker);

        String displayName = currentUser.getUsername();
        if (displayName.length() > 24) {
            displayName = displayName.substring(0, 23) + "\u2026";
        }
        welcomeLabel = new JLabel("Hey, " + displayName);
        welcomeLabel.setFont(GameHubTheme.fontTitle());
        welcomeLabel.setForeground(GameHubTheme.TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftHead.add(Box.createVerticalStrut(4));
        leftHead.add(welcomeLabel);

        JLabel sub = new JLabel("<html><body style='width:420px'>Pick a game. Scores and time are saved to your profile.</body></html>");
        sub.setFont(GameHubTheme.fontSubtitle());
        sub.setForeground(GameHubTheme.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftHead.add(Box.createVerticalStrut(6));
        leftHead.add(sub);

        header.add(leftHead, BorderLayout.WEST);

        JButton logoutButton = GameHubTheme.createGhostButton("Sign out");
        logoutButton.setFont(GameHubTheme.fontBodyBold());
        logoutButton.addActionListener(e -> handleLogout());
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(logoutButton);
        header.add(right, BorderLayout.EAST);

        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(new EmptyBorder(0, 28, 8, 28));

        totalScoreLabel = new JLabel(String.valueOf(currentUser.getTotalScore()));
        long hours = currentUser.getTotalPlayTime() / 3600000;
        long minutes = (currentUser.getTotalPlayTime() % 3600000) / 60000;
        totalPlayTimeLabel = new JLabel(hours + "h " + minutes + "m");

        statsRow.add(buildStatChip("Total score", totalScoreLabel, GameHubTheme.ACCENT_CYAN));
        statsRow.add(buildStatChip("Play time", totalPlayTimeLabel, GameHubTheme.ACCENT_AMBER));

        JPanel pickHeader = new JPanel(new BorderLayout());
        pickHeader.setOpaque(false);
        pickHeader.setBorder(new EmptyBorder(8, 28, 12, 28));
        JLabel pickTitle = new JLabel("Game select");
        pickTitle.setFont(GameHubTheme.fontCaption());
        pickTitle.setForeground(GameHubTheme.TEXT_MUTED);
        JLabel pickBig = new JLabel("Choose a mode");
        pickBig.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        pickBig.setForeground(GameHubTheme.TEXT_PRIMARY);
        JPanel pickLeft = new JPanel();
        pickLeft.setLayout(new BoxLayout(pickLeft, BoxLayout.Y_AXIS));
        pickLeft.setOpaque(false);
        pickLeft.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GameHubTheme.ACCENT_MAGENTA));
        pickLeft.add(pickTitle);
        pickLeft.add(Box.createVerticalStrut(4));
        pickLeft.add(pickBig);
        pickHeader.add(pickLeft, BorderLayout.WEST);

        gamesPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        gamesPanel.setOpaque(false);
        gamesPanel.setBorder(new EmptyBorder(8, 28, 32, 28));
        gamesPanel.setPreferredSize(new Dimension(1000, 320));
        gamesPanel.setMinimumSize(new Dimension(720, 260));

        addGameTile("Tic Tac Toe", "Classic grid duel", "TicTacToe", GameHubTheme.ACCENT_CYAN);
        addGameTile("Snake", "Eat, grow, survive", "Snake", GameHubTheme.ACCENT_GREEN);
        addGameTile("Number Guess", "1–100 in 10 tries", "NumberGuessing", GameHubTheme.ACCENT_AMBER);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.add(statsRow);
        center.add(pickHeader);
        center.add(gamesPanel);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    private JPanel buildStatChip(String label, JLabel valueLabel, Color accent) {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GameHubTheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(accent);
                g2.fillRoundRect(0, 0, 4, getHeight(), 2, 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel top = new JLabel(label.toUpperCase());
        top.setFont(GameHubTheme.fontCaption());
        top.setForeground(GameHubTheme.TEXT_MUTED);

        valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        valueLabel.setForeground(GameHubTheme.TEXT_PRIMARY);

        JPanel inner = new JPanel(new BorderLayout(0, 4));
        inner.setOpaque(false);
        inner.add(top, BorderLayout.NORTH);
        inner.add(valueLabel, BorderLayout.CENTER);
        wrap.add(inner, BorderLayout.CENTER);

        Dimension d = wrap.getPreferredSize();
        wrap.setPreferredSize(new Dimension(Math.max(200, d.width + 8), 86));
        return wrap;
    }

    private void addGameTile(String title, String tagline, String gameClass, Color accent) {
        JButton tile = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                Color base = GameHubTheme.BG_CARD;
                if (getModel().isPressed()) {
                    base = new Color(24, 28, 48);
                } else if (getModel().isRollover()) {
                    base = GameHubTheme.BG_ELEVATED;
                }
                g2d.setColor(base);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
                g2d.setColor(GameHubTheme.BORDER_LINE);
                g2d.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);
                if (getModel().isRollover() && isEnabled()) {
                    g2d.setStroke(new BasicStroke(2f));
                    g2d.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 160));
                    g2d.drawRoundRect(1, 1, w - 3, h - 3, 20, 20);
                    g2d.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 28));
                    g2d.fillRect(0, h - 48, w, 48);
                }

                Font hudF = GameHubTheme.fontHud();
                Font titleF = new Font(Font.SANS_SERIF, Font.BOLD, 17);
                Font capF = GameHubTheme.fontCaption();
                g2d.setFont(capF);
                int capH = g2d.getFontMetrics().getHeight();
                g2d.setFont(titleF);
                int titleH = g2d.getFontMetrics().getHeight();
                g2d.setFont(hudF);
                int hudH = g2d.getFontMetrics().getHeight();
                int footerBlock = hudH + titleH + capH + 36;
                int artBottom = Math.max(80, h - footerBlock);

                switch (gameClass) {
                    case "TicTacToe":
                        paintTicPreview(g2d, w, h, artBottom);
                        break;
                    case "Snake":
                        paintSnakePreview(g2d, w, h, artBottom);
                        break;
                    case "NumberGuessing":
                        paintGuessPreview(g2d, w, h, artBottom);
                        break;
                    default:
                        break;
                }

                int tx = 18;
                int baseline = h - 14;
                g2d.setFont(capF);
                g2d.setColor(GameHubTheme.TEXT_MUTED);
                g2d.drawString(tagline, tx, baseline);
                baseline -= capH + 6;
                g2d.setFont(titleF);
                g2d.setColor(GameHubTheme.TEXT_PRIMARY);
                g2d.drawString(title, tx, baseline);
                baseline -= titleH + 8;
                g2d.setFont(hudF);
                g2d.setColor(accent);
                g2d.drawString("PLAY", tx, baseline);

                g2d.dispose();
            }
        };

        tile.setBorder(new EmptyBorder(10, 10, 10, 10));
        tile.setContentAreaFilled(false);
        tile.setOpaque(false);
        tile.setFocusPainted(false);
        tile.setRolloverEnabled(true);
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tile.setPreferredSize(new Dimension(300, 320));
        tile.setMinimumSize(new Dimension(220, 260));
        tile.addActionListener(e -> startGame(gameClass));
        gamesPanel.add(tile);
    }

    private void paintTicPreview(Graphics2D g2d, int w, int h, int maxBottom) {
        int top = 18;
        int maxH = maxBottom - top - 6;
        if (maxH < 36) {
            return;
        }
        int board = Math.min(w - 40, maxH);
        int cell = board / 3;
        int sx = (w - board) / 2;
        int sy = top;
        g2d.setColor(new Color(255, 255, 255, 16));
        g2d.fillRoundRect(sx - 6, sy - 6, board + 12, board + 12, 10, 10);
        g2d.setColor(GameHubTheme.TEXT_PRIMARY);
        g2d.setStroke(new BasicStroke(2f));
        for (int i = 1; i < 3; i++) {
            g2d.drawLine(sx, sy + i * cell, sx + board, sy + i * cell);
            g2d.drawLine(sx + i * cell, sy, sx + i * cell, sy + board);
        }
        int r = Math.max(3, cell / 5);
        drawMiniX(g2d, sx + cell / 2, sy + cell / 2, r);
        drawMiniO(g2d, sx + cell + cell / 2, sy + cell / 2, r);
        drawMiniO(g2d, sx + cell / 2, sy + cell + cell / 2, r);
        drawMiniX(g2d, sx + cell + cell / 2, sy + cell + cell / 2, r);
        g2d.setColor(GameHubTheme.ACCENT_CYAN);
        g2d.setStroke(new BasicStroke(2.5f));
        drawMiniX(g2d, sx + 2 * cell + cell / 2, sy + 2 * cell + cell / 2, r);
    }

    private void drawMiniX(Graphics2D g, int cx, int cy, int s) {
        g.drawLine(cx - s, cy - s, cx + s, cy + s);
        g.drawLine(cx - s, cy + s, cx + s, cy - s);
    }

    private void drawMiniO(Graphics2D g, int cx, int cy, int r) {
        g.drawOval(cx - r, cy - r, r * 2, r * 2);
    }

    private void paintSnakePreview(Graphics2D g2d, int w, int h, int maxBottom) {
        int cols = 12;
        int rows = 8;
        int top = 16;
        int gh = Math.max(40, maxBottom - top - 12);
        int gw = w - 56;
        int cell = Math.min(gw / cols, gh / rows);
        if (cell < 4) {
            return;
        }
        int ox = (w - cols * cell) / 2;
        int oy = top;
        g2d.setColor(GameHubTheme.BG_ELEVATED);
        g2d.fillRoundRect(ox - 6, oy - 6, cols * cell + 12, rows * cell + 12, 10, 10);
        for (int i = 0; i < 5; i++) {
            int x = ox + (4 - i) * cell;
            int y = oy + 4 * cell;
            g2d.setColor(i == 0 ? GameHubTheme.ACCENT_GREEN : GameHubTheme.ACCENT_CYAN_DIM);
            g2d.fillRoundRect(x + 1, y + 1, cell - 2, cell - 2, 6, 6);
        }
        int fx = ox + 2 * cell;
        int fy = oy + 2 * cell;
        g2d.setColor(GameHubTheme.ACCENT_MAGENTA);
        g2d.fillOval(fx + 2, fy + 2, cell - 4, cell - 4);
    }

    private void paintGuessPreview(Graphics2D g2d, int w, int h, int maxBottom) {
        int midY = Math.min(maxBottom - 36, 58);
        g2d.setColor(GameHubTheme.BG_ELEVATED);
        g2d.fillRoundRect(w / 2 - 68, midY - 28, 136, 50, 12, 12);
        g2d.setColor(GameHubTheme.ACCENT_AMBER);
        g2d.setFont(GameHubTheme.fontMono());
        String t = "? 42";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(t, (w - fm.stringWidth(t)) / 2, midY + 6);
        g2d.setColor(GameHubTheme.TEXT_MUTED);
        g2d.setFont(GameHubTheme.fontCaption());
        String sub = "High / low hints";
        g2d.drawString(sub, (w - g2d.getFontMetrics().stringWidth(sub)) / 2, midY + 26);
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
            default:
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
            totalScoreLabel.setText(String.valueOf(currentUser.getTotalScore()));
            long hours = currentUser.getTotalPlayTime() / 3600000;
            long minutes = (currentUser.getTotalPlayTime() % 3600000) / 60000;
            totalPlayTimeLabel.setText(hours + "h " + minutes + "m");
        }
    }
}

package gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Arcade / gaming-hub visual language: deep space background, neon accents, HUD-style type.
 */
public final class GameHubTheme {

    private GameHubTheme() {}

    public static final Color BG_DEEP = new Color(10, 11, 18);
    public static final Color BG_PANEL = new Color(22, 26, 42);
    public static final Color BG_CARD = new Color(30, 36, 56);
    public static final Color BG_ELEVATED = new Color(42, 50, 78);

    public static final Color ACCENT_CYAN = new Color(0, 230, 255);
    public static final Color ACCENT_CYAN_DIM = new Color(0, 168, 196);
    public static final Color ACCENT_MAGENTA = new Color(255, 72, 196);
    public static final Color ACCENT_AMBER = new Color(255, 196, 92);
    public static final Color ACCENT_GREEN = new Color(64, 240, 172);
    public static final Color ACCENT_VIOLET = new Color(150, 110, 255);

    public static final Color TEXT_PRIMARY = new Color(240, 244, 255);
    public static final Color TEXT_MUTED = new Color(128, 138, 175);
    public static final Color TEXT_ERROR = new Color(255, 118, 140);
    public static final Color TEXT_SUCCESS = new Color(96, 246, 186);

    public static final Color BORDER_LINE = new Color(56, 66, 102);
    public static final Color BORDER_FOCUS = ACCENT_CYAN;

    public static void installGlobalUiHints() {
        Color panel = BG_PANEL;
        UIManager.put("OptionPane.background", panel);
        UIManager.put("Panel.background", panel);
        UIManager.put("Label.background", panel);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.font", fontBody());
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("OptionPane.font", fontBody());
        UIManager.put("Button.background", BG_ELEVATED);
        UIManager.put("Button.foreground", TEXT_PRIMARY);
        UIManager.put("Button.font", fontBodyBold());
        UIManager.put("TextField.background", BG_ELEVATED);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", ACCENT_CYAN);
        UIManager.put("PasswordField.background", BG_ELEVATED);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("ProgressBar.background", BG_ELEVATED);
        UIManager.put("ProgressBar.foreground", ACCENT_CYAN);
        UIManager.put("ProgressBar.selectionBackground", ACCENT_CYAN);
        UIManager.put("ProgressBar.selectionForeground", BG_DEEP);
        UIManager.put("control", panel);
        UIManager.put("text", TEXT_PRIMARY);
    }

    /** Large wordmark: "GAMING" + "HUB" (no HTML sizing quirks). */
    public static JPanel createBrandTitleRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        p.setOpaque(false);
        JLabel a = new JLabel("GAMING");
        a.setFont(fontBrand());
        a.setForeground(ACCENT_CYAN);
        JLabel b = new JLabel("HUB");
        b.setFont(fontBrand());
        b.setForeground(ACCENT_MAGENTA);
        p.add(a);
        p.add(b);
        return p;
    }

    /** Two-word colored title row (e.g. register / games). */
    public static JPanel createTwoWordTitle(String left, Color leftColor, String right, Color rightColor, int fontSize) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        p.setOpaque(false);
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
        JLabel l = new JLabel(left);
        l.setFont(f);
        l.setForeground(leftColor);
        JLabel r = new JLabel(right);
        r.setFont(f);
        r.setForeground(rightColor);
        p.add(l);
        p.add(r);
        return p;
    }

    /** Three short tokens on one row (Tic · Tac · Toe). */
    public static JPanel createTripleWordTitle(String a, Color ca, String b, Color cb, String c, Color cc, int fontSize) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        p.setOpaque(false);
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
        JLabel la = new JLabel(a);
        la.setFont(f);
        la.setForeground(ca);
        JLabel lb = new JLabel(b);
        lb.setFont(f);
        lb.setForeground(cb);
        JLabel lc = new JLabel(c);
        lc.setFont(f);
        lc.setForeground(cc);
        p.add(la);
        p.add(lb);
        p.add(lc);
        return p;
    }

    public static Font fontBrand() {
        return new Font(Font.SANS_SERIF, Font.BOLD, 30);
    }

    public static Font fontTitle() {
        return new Font(Font.SANS_SERIF, Font.BOLD, 24);
    }

    public static Font fontSubtitle() {
        return new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    }

    public static Font fontBody() {
        return new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    }

    public static Font fontBodyBold() {
        return new Font(Font.SANS_SERIF, Font.BOLD, 14);
    }

    public static Font fontCaption() {
        return new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    }

    /** Small caps–style label; slightly larger than before for legibility. */
    public static Font fontHud() {
        return new Font(Font.SANS_SERIF, Font.BOLD, 11);
    }

    public static Font fontMono() {
        return new Font(Font.MONOSPACED, Font.BOLD, 15);
    }

    public static void paintHubGradient(Graphics2D g2d, int width, int height) {
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setPaint(new GradientPaint(0, 0, new Color(16, 20, 42), width, height, new Color(8, 9, 16)));
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(new Color(255, 64, 200, 8));
        g2d.fillOval(width / 2 - 200, -120, 420, 280);
        g2d.setColor(new Color(0, 220, 255, 10));
        g2d.fillOval(-80, height / 2, 360, 360);

        g2d.setColor(new Color(0, 220, 255, 10));
        for (int i = -height; i < width; i += 64) {
            g2d.drawLine(i, 0, i + height + 40, height);
        }

        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRect(0, 0, width, height / 6);
        g2d.fillRect(0, height - height / 8, width, height / 8);
        g2d.setColor(new Color(0, 0, 0, 45));
        for (int y = 0; y < height; y += 4) {
            g2d.fillRect(0, y, width, 1);
        }
    }

    public static Border fieldBorder(boolean focused) {
        Color line = focused ? BORDER_FOCUS : BORDER_LINE;
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(line, focused ? 2 : 1),
                new EmptyBorder(11, 14, 11, 14));
    }

    public static void styleTextField(JTextField field) {
        field.setFont(fontBody());
        field.setBackground(BG_ELEVATED);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_CYAN);
        field.setSelectionColor(new Color(0, 220, 255, 90));
        field.setSelectedTextColor(BG_DEEP);
        field.setBorder(fieldBorder(false));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(fieldBorder(true));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(fieldBorder(false));
            }
        });
    }

    public static void styleGameProgressBar(JProgressBar bar) {
        bar.setFont(fontCaption());
        bar.setForeground(ACCENT_CYAN);
        bar.setBackground(BG_ELEVATED);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LINE, 1),
                new EmptyBorder(4, 8, 4, 8)));
        bar.setStringPainted(true);
        bar.setPreferredSize(new Dimension(200, 28));
    }

    public static JButton createPillButton(String text, Color normal, Color hover) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = normal;
                if (!isEnabled()) {
                    fill = new Color(
                            Math.min(255, fill.getRed() + 40),
                            Math.min(255, fill.getGreen() + 40),
                            Math.min(255, fill.getBlue() + 40));
                } else if (getModel().isPressed()) {
                    fill = hover.darker();
                } else if (getModel().isRollover()) {
                    fill = hover;
                }
                g2d.setColor(fill);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                if (isEnabled() && getModel().isRollover()) {
                    g2d.setStroke(new BasicStroke(2f));
                    g2d.setColor(new Color(0, 255, 255, 90));
                    g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                }
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(fontBodyBold());
        button.setForeground(BG_DEEP);
        button.setBackground(normal);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(14, 22, 14, 22));
        button.setMargin(new Insets(2, 6, 2, 6));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setRolloverEnabled(true);
        button.setMinimumSize(new Dimension(120, 46));
        return button;
    }

    public static JButton createGhostButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int a = getModel().isRollover() ? 44 : 26;
                if (getModel().isPressed()) {
                    a = 58;
                }
                g2d.setColor(new Color(255, 255, 255, a));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2d.setColor(getModel().isRollover() ? ACCENT_CYAN : BORDER_LINE);
                g2d.setStroke(new BasicStroke(getModel().isRollover() ? 2f : 1f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(fontBodyBold());
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setMargin(new Insets(2, 6, 2, 6));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setRolloverEnabled(true);
        button.setMinimumSize(new Dimension(100, 44));
        return button;
    }

    public static JButton createToolbarButton(String text, Color bg, Color bgHover) {
        JButton b = createPillButton(text, bg, bgHover);
        b.setForeground(TEXT_PRIMARY);
        return b;
    }

    public static JLabel mutedLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(fontCaption());
        l.setForeground(TEXT_MUTED);
        return l;
    }

    public static JLabel createHudBadge(String text) {
        JLabel b = new JLabel(text.toUpperCase());
        b.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        b.setForeground(BG_DEEP);
        b.setOpaque(true);
        b.setBackground(ACCENT_CYAN);
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        return b;
    }

    public static Border neonCardPadding(int top, int left, int bottom, int right) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 220, 255, 72), 1),
                new EmptyBorder(top, left, bottom, right));
    }

    public static void showArcadeDialog(Component parent, String title, String message, int type) {
        JOptionPane.showMessageDialog(parent, message, title, type);
    }

    /** Multi-line notice that wraps and does not clip like fixed-width HTML. */
    public static JTextArea createWrappedNotice(String text, Color fg) {
        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);
        ta.setFocusable(false);
        ta.setOpaque(false);
        ta.setForeground(fg);
        ta.setFont(fontCaption());
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(new EmptyBorder(0, 0, 8, 0));
        ta.setColumns(36);
        ta.setRows(0);
        return ta;
    }
}

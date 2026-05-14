package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import database.DatabaseManager;
import services.AuthenticationService;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

    public LoginPage() {
        setTitle("Gaming Hub — Sign in");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(560, 560));
        setSize(620, 640);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                GameHubTheme.paintHubGradient((Graphics2D) g, getWidth(), getHeight());
            }
        };
        root.setOpaque(true);

        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(true);
        card.setBackground(GameHubTheme.BG_CARD);
        card.setBorder(GameHubTheme.neonCardPadding(28, 36, 32, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        int y = 0;

        if (!DatabaseManager.isReady()) {
            String err = DatabaseManager.getLastInitError();
            String msg = "Database offline.\n\n"
                    + (err != null ? err + "\n\n" : "")
                    + "The app can download the driver once, or add sqlite-jdbc-3.43.0.0.jar to the classpath (see build.sh).";
            JTextArea dbWarn = GameHubTheme.createWrappedNotice(msg, GameHubTheme.TEXT_ERROR);
            gbc.gridy = y++;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.insets = new Insets(0, 4, 12, 4);
            card.add(dbWarn, gbc);
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
        }

        JLabel badge = GameHubTheme.createHudBadge("Player login");
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 8, 12, 8);
        card.add(badge, gbc);

        JPanel titleRow = GameHubTheme.createBrandTitleRow();
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 8, 6, 8);
        card.add(titleRow, gbc);

        JLabel subtitleLabel = new JLabel("<html><center>Your scores and play time stay on this device.</center></html>");
        subtitleLabel.setFont(GameHubTheme.fontSubtitle());
        subtitleLabel.setForeground(GameHubTheme.TEXT_MUTED);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 8, 20, 8);
        card.add(subtitleLabel, gbc);
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.anchor = GridBagConstraints.WEST;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(GameHubTheme.TEXT_MUTED);
        usernameLabel.setFont(GameHubTheme.fontCaption());
        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 8, 4, 8);
        card.add(usernameLabel, gbc);

        usernameField = new JTextField(24);
        GameHubTheme.styleTextField(usernameField);
        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 12, 8);
        card.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(GameHubTheme.TEXT_MUTED);
        passwordLabel.setFont(GameHubTheme.fontCaption());
        gbc.gridy = y++;
        gbc.insets = new Insets(4, 8, 4, 8);
        card.add(passwordLabel, gbc);

        passwordField = new JPasswordField(24);
        GameHubTheme.styleTextField(passwordField);
        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 8, 8);
        card.add(passwordField, gbc);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(GameHubTheme.fontCaption());
        statusLabel.setForeground(GameHubTheme.TEXT_ERROR);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = y++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(4, 8, 8, 8);
        card.add(statusLabel, gbc);
        gbc.weightx = 0;

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 14, 0));
        buttonPanel.setOpaque(false);

        loginButton = GameHubTheme.createPillButton("Sign in", GameHubTheme.ACCENT_CYAN, GameHubTheme.ACCENT_CYAN_DIM);
        loginButton.addActionListener(this::handleLogin);
        buttonPanel.add(loginButton);

        registerButton = GameHubTheme.createPillButton("Create account", GameHubTheme.ACCENT_MAGENTA, new Color(220, 50, 150));
        registerButton.setForeground(GameHubTheme.TEXT_PRIMARY);
        registerButton.addActionListener(this::handleRegister);
        buttonPanel.add(registerButton);

        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(12, 8, 0, 8);
        card.add(buttonPanel, gbc);

        JLabel hint = new JLabel("Press Enter to sign in");
        hint.setFont(GameHubTheme.fontCaption());
        hint.setForeground(GameHubTheme.TEXT_MUTED);
        gbc.gridy = y++;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(14, 8, 0, 8);
        card.add(hint, gbc);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(20, 24, 24, 24);
        centerWrap.add(card, c);

        root.add(centerWrap, BorderLayout.CENTER);

        add(root);
        getRootPane().setDefaultButton(loginButton);
        setVisible(true);
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setForeground(GameHubTheme.TEXT_ERROR);
            statusLabel.setText("Please enter both username and password.");
            return;
        }

        if (!DatabaseManager.isReady()) {
            String err = DatabaseManager.getLastInitError();
            statusLabel.setForeground(GameHubTheme.TEXT_ERROR);
            statusLabel.setText(err != null ? err : "Database offline — cannot sign in.");
            return;
        }

        if (AuthenticationService.login(username, password)) {
            statusLabel.setText(" ");
            dispose();
            new Dashboard();
        } else {
            statusLabel.setForeground(GameHubTheme.TEXT_ERROR);
            statusLabel.setText("Invalid username or password.");
            passwordField.setText("");
        }
    }

    private void handleRegister(ActionEvent e) {
        dispose();
        new RegisterPage();
    }
}

package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import database.DatabaseManager;
import services.AuthenticationService;

public class RegisterPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;

    public RegisterPage() {
        setTitle("Gaming Hub — Create account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(560, 600));
        setSize(620, 720);
        setLocationRelativeTo(null);
        setResizable(true);

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
        card.setBorder(GameHubTheme.neonCardPadding(24, 36, 28, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        int y = 0;

        if (!DatabaseManager.isReady()) {
            String err = DatabaseManager.getLastInitError();
            String msg = "Database offline.\n\n" + (err != null ? err + "\n\n" : "")
                    + "Fix the driver or allow a one-time download, then restart.";
            JTextArea dbWarn = GameHubTheme.createWrappedNotice(msg, GameHubTheme.TEXT_ERROR);
            gbc.gridy = y++;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.insets = new Insets(0, 4, 12, 4);
            card.add(dbWarn, gbc);
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.NONE;
        }

        JLabel badge = GameHubTheme.createHudBadge("New save");
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 8, 10, 8);
        card.add(badge, gbc);

        JPanel titleRow = GameHubTheme.createTwoWordTitle(
                "NEW", GameHubTheme.ACCENT_GREEN,
                "PLAYER", GameHubTheme.TEXT_PRIMARY,
                26);
        gbc.gridy = y++;
        gbc.insets = new Insets(0, 8, 6, 8);
        card.add(titleRow, gbc);

        JLabel subtitleLabel = new JLabel("<html><center>Create a username and password, then enter the lobby.</center></html>");
        subtitleLabel.setFont(GameHubTheme.fontSubtitle());
        subtitleLabel.setForeground(GameHubTheme.TEXT_MUTED);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 8, 16, 8);
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
        gbc.insets = new Insets(6, 8, 4, 8);
        card.add(usernameLabel, gbc);

        usernameField = new JTextField(24);
        GameHubTheme.styleTextField(usernameField);
        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 10, 8);
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
        gbc.insets = new Insets(0, 8, 10, 8);
        card.add(passwordField, gbc);

        JLabel confirmLabel = new JLabel("Confirm password");
        confirmLabel.setForeground(GameHubTheme.TEXT_MUTED);
        confirmLabel.setFont(GameHubTheme.fontCaption());
        gbc.gridy = y++;
        gbc.insets = new Insets(4, 8, 4, 8);
        card.add(confirmLabel, gbc);

        confirmPasswordField = new JPasswordField(24);
        GameHubTheme.styleTextField(confirmPasswordField);
        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 8, 8);
        card.add(confirmPasswordField, gbc);

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

        registerButton = GameHubTheme.createPillButton("Register & play", GameHubTheme.ACCENT_GREEN, new Color(52, 196, 132));
        registerButton.addActionListener(this::handleRegister);
        buttonPanel.add(registerButton);

        backButton = GameHubTheme.createGhostButton("Back to sign in");
        backButton.addActionListener(this::handleBack);
        buttonPanel.add(backButton);

        gbc.gridy = y++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 8, 0, 8);
        card.add(buttonPanel, gbc);

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(16, 24, 24, 24);
        centerWrap.add(card, c);

        root.add(centerWrap, BorderLayout.CENTER);

        add(root);
        getRootPane().setDefaultButton(registerButton);
        setVisible(true);
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    private void handleRegister(ActionEvent e) {
        if (!DatabaseManager.isReady()) {
            statusLabel.setForeground(GameHubTheme.TEXT_ERROR);
            statusLabel.setText(DatabaseManager.getLastInitError() != null
                    ? DatabaseManager.getLastInitError()
                    : "Database offline — cannot register.");
            return;
        }

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setForeground(GameHubTheme.TEXT_ERROR);
            statusLabel.setText("Please complete every field.");
            return;
        }

        if (AuthenticationService.register(username, password, confirmPassword)) {
            if (!AuthenticationService.login(username, password)) {
                statusLabel.setForeground(GameHubTheme.TEXT_ERROR);
                statusLabel.setText("Account created — go back and sign in manually.");
                registerButton.setEnabled(true);
                backButton.setEnabled(true);
                return;
            }
            statusLabel.setForeground(GameHubTheme.TEXT_SUCCESS);
            statusLabel.setText("Welcome — opening lobby…");
            registerButton.setEnabled(false);
            backButton.setEnabled(false);

            Timer t = new Timer(900, ev -> {
                ((Timer) ev.getSource()).stop();
                dispose();
                new Dashboard();
            });
            t.setRepeats(false);
            t.start();
        } else {
            statusLabel.setForeground(GameHubTheme.TEXT_ERROR);
            statusLabel.setText("Username taken or invalid (min 3 chars, password 4+).");
            passwordField.setText("");
            confirmPasswordField.setText("");
        }
    }

    private void handleBack(ActionEvent e) {
        dispose();
        new LoginPage();
    }
}

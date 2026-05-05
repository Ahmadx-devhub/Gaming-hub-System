package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import services.AuthenticationService;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

    public LoginPage() {
        setTitle("Gaming Hub - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with soft background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(248, 249, 250)); // Modern light background
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);

        // Title
        JLabel titleLabel = new JLabel("Gaming Hub");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(new Color(20, 30, 60)); // Professional dark blue
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Welcome Back");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(120, 120, 128)); // Medium gray
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 25, 25, 25);
        mainPanel.add(subtitleLabel, gbc);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(50, 50, 60));
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 25, 8, 25);
        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBackground(new Color(255, 255, 255));
        usernameField.setForeground(new Color(20, 20, 25));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        usernameField.setCaretColor(new Color(80, 150, 220));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 25, 15, 25);
        mainPanel.add(usernameField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(50, 50, 60));
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 25, 8, 25);
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setForeground(new Color(20, 20, 25));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        passwordField.setCaretColor(new Color(80, 150, 220));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 25, 15, 25);
        mainPanel.add(passwordField, gbc);

        // Status label
        statusLabel = new JLabel("");
        statusLabel.setForeground(new Color(200, 70, 80));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 25, 10, 25);
        mainPanel.add(statusLabel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        // Login button
        loginButton = createModernButton("Login", new Color(59, 130, 246), new Color(37, 99, 235));
        loginButton.addActionListener(this::handleLogin);
        buttonPanel.add(loginButton);

        // Register button
        registerButton = createModernButton("Register", new Color(34, 197, 94), new Color(22, 163, 74));
        registerButton.addActionListener(this::handleRegister);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 25, 25, 25);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createModernButton(String text, Color normalColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(normalColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normalColor);
            }
        });

        return button;
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            return;
        }

        if (AuthenticationService.login(username, password)) {
            statusLabel.setText("");
            dispose();
            new Dashboard();
        } else {
            statusLabel.setText("Invalid username or password");
            passwordField.setText("");
        }
    }

    private void handleRegister(ActionEvent e) {
        dispose();
        new RegisterPage();
    }
}

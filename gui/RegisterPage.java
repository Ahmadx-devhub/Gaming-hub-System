package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import services.AuthenticationService;

public class RegisterPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel statusLabel;

    public RegisterPage() {
        setTitle("Gaming Hub - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel with soft background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(248, 249, 250)); // Modern light background
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(new Color(20, 30, 60)); // Professional dark blue
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Join Gaming Hub");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(120, 120, 128));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 25, 25, 25);
        mainPanel.add(subtitleLabel, gbc);

        // Username
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

        // Password
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

        // Confirm Password
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setForeground(new Color(50, 50, 60));
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 25, 8, 25);
        mainPanel.add(confirmLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setBackground(new Color(255, 255, 255));
        confirmPasswordField.setForeground(new Color(20, 20, 25));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        confirmPasswordField.setCaretColor(new Color(80, 150, 220));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 25, 15, 25);
        mainPanel.add(confirmPasswordField, gbc);

        // Status label
        statusLabel = new JLabel("");
        statusLabel.setForeground(new Color(200, 70, 80));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 25, 10, 25);
        mainPanel.add(statusLabel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);

        // Register button
        registerButton = createModernButton("Register", new Color(34, 197, 94), new Color(22, 163, 74));
        registerButton.addActionListener(this::handleRegister);
        buttonPanel.add(registerButton);

        // Back button
        backButton = createModernButton("Back to Login", new Color(107, 114, 128), new Color(75, 85, 99));
        backButton.addActionListener(this::handleBack);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
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

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            return;
        }

        if (AuthenticationService.register(username, password, confirmPassword)) {
            statusLabel.setForeground(new Color(80, 150, 100)); // Soft green
            statusLabel.setText("Registration successful! Logging in...");
            
            // Auto-login after successful registration
            AuthenticationService.login(username, password);
            
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000);
                    dispose();
                    new Dashboard();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        } else {
            statusLabel.setForeground(new Color(220, 80, 80));
            statusLabel.setText("Username exists or invalid input");
            passwordField.setText("");
            confirmPasswordField.setText("");
        }
    }

    private void handleBack(ActionEvent e) {
        dispose();
        new LoginPage();
    }
}

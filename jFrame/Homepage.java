package jFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Homepage {
    private JPanel homepagePanel;
    private JButton loginButton;
    private JButton registratiButton;
    private JLabel titleLabel;
    private JLabel logoLabel;

    public Homepage() {
        // Inizializzazione pannello principale
        homepagePanel = new JPanel();
        homepagePanel.setBackground(new Color(245, 250, 255));
        homepagePanel.setLayout(new BoxLayout(homepagePanel, BoxLayout.Y_AXIS));
        homepagePanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Logo
        logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo.png"));
            logoLabel.setIcon(logo);
        } catch (Exception e) {
            logoLabel.setText("LOGO");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            logoLabel.setForeground(new Color(100, 100, 100));
        }

        // Titolo
        titleLabel = new JLabel("Benvenuto");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Pulsante Login
        loginButton = new JButton("Login");
        styleButton(loginButton, new Color(70, 130, 180));

        // Pulsante Registrati
        registratiButton = new JButton("Registrati");
        styleButton(registratiButton, new Color(60, 179, 113));

        // Aggiunta componenti
        homepagePanel.add(logoLabel);
        homepagePanel.add(titleLabel);
        homepagePanel.add(Box.createVerticalStrut(10));
        homepagePanel.add(loginButton);
        homepagePanel.add(Box.createVerticalStrut(10));
        homepagePanel.add(registratiButton);

        // Eventi
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(homepagePanel);
                frame.setContentPane(new LoginPage(frame).getLoginPanel());
                frame.revalidate();
                frame.repaint();
            }
        });

        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(homepagePanel);
                frame.setContentPane(new RegisterPage(frame).getRegisterPanel());
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    public JPanel getHomepagePanel() {
        return homepagePanel;
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(200, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}

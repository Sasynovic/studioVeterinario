package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import controller.LoginResult;
import controller.UtenteController;

public class LoginPage {
    private JPanel loginPanel;
    private JTextField usernameTextField;
    private JPasswordField passwordPasswordField;
    private JButton loginButton;
    private JButton backButton;
    private JLabel titleLabel;

    private JFrame frame; // riferimento al frame principale

    private static Utilities utilities = new Utilities();

    public LoginPage(JFrame frame) {
        this.frame = frame;

        loginPanel = new JPanel();
        loginPanel.setBackground(new Color(245, 250, 255));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        titleLabel = new JLabel("Login");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        usernameTextField = new JTextField();
        usernameTextField.setMaximumSize(new Dimension(300, 40));
        usernameTextField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameTextField.setBorder(BorderFactory.createTitledBorder("Username"));

        passwordPasswordField = new JPasswordField();
        passwordPasswordField.setMaximumSize(new Dimension(300, 40));
        passwordPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordPasswordField.setBorder(BorderFactory.createTitledBorder("Password"));

        loginButton = utilities.createButton("Login", utilities.Blue);

        backButton = utilities.createButton("Indietro", utilities.Red);

        loginPanel.add(titleLabel);
        loginPanel.add(usernameTextField);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(passwordPasswordField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(backButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText().trim();
                String password = new String(passwordPasswordField.getPassword()).trim();

                // Validazione input
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Inserisci username e password",
                            "Campi mancanti",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                UtenteController loginc = new UtenteController();
                try {
                    LoginResult user = loginc.login(username, password);

                    if (user.isSuccess()) {
                        JOptionPane.showMessageDialog(frame,
                                "Login effettuato con successo!",
                                "Benvenuto",
                                JOptionPane.INFORMATION_MESSAGE);

                        int tipo = user.getTipoUtente();

                        switch (tipo) {
                            case 1: // Amministratore
                                frame.setContentPane(new AdminCMS(frame).getAdminPanel());
                                frame.revalidate();
                                frame.repaint();
                                break;
                            case 2: // Veterinario
                                frame.setContentPane(new VetCMS(frame, user.getUsername()).getvetPanel());
                                frame.revalidate();
                                frame.repaint();
                                break;
                            case 3: // Proprietario
                                frame.setContentPane(new ProCMS(frame, user.getNome(), user.getCognome(), user.getUsername() ).getProPanel());
                                frame.revalidate();
                                frame.repaint();
                                break;
                            default: // Utente non riconosciuto
                                JOptionPane.showMessageDialog(frame,
                                        user.getMessage(),
                                        "Errore di login",
                                        JOptionPane.ERROR_MESSAGE);
                                // Pulisci il campo password
                                passwordPasswordField.setText("");
                                usernameTextField.requestFocus();
                                break;
                        }

                    } else {
                        JOptionPane.showMessageDialog(frame,
                                user.getMessage(),
                                "Errore di login",
                                JOptionPane.ERROR_MESSAGE);
                        // Pulisci il campo password
                        passwordPasswordField.setText("");
                        usernameTextField.requestFocus();
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Errore durante il login: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Errore nel sistema",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Torna alla homepage: sostituisci il content pane con la homepage
                frame.setContentPane(new Homepage().getHomepagePanel());
                frame.revalidate();
                frame.repaint();
            }
        });
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }
}

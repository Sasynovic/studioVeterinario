package boundary;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.regex.Pattern;

import database.UtenteDAO;

public class RegisterPage {
    private JPanel registerPanel;
    private JTextField usernameTextField;
    private JPasswordField passwordPasswordField;
    private JPasswordField ripetiPasswordPasswordField;
    private JTextField nomeTextField;
    private JTextField cognomeTextField;
    private JTextField emailTextField;
    private JTextField immagineProfiloTextField;
    private JButton scegliImmagineButton;

    private JButton registratiButton;
    private JButton backButton;
    private JCheckBox mostraPasswordCheckBox;

    // Labels per messaggi di errore/validazione
    private JLabel usernameStatusLabel;
    private JLabel passwordStatusLabel;
    private JLabel emailStatusLabel;
    private JLabel confirmPasswordLabel;

    private JFrame frame;

    // Pattern per validazione email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public RegisterPage(JFrame frame) {
        this.frame = frame;
        initializeComponents();
        setupLayout();
        setupValidation();
        setupEventHandlers();
    }

    private void initializeComponents() {
        registerPanel = new JPanel();
        registerPanel.setBackground(new Color(245, 250, 255));
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Campi di testo con validazione
        usernameTextField = createTextField("Username");
        passwordPasswordField = createPasswordField("Password");
        ripetiPasswordPasswordField = createPasswordField("Ripeti Password");
        nomeTextField = createTextField("Nome");
        cognomeTextField = createTextField("Cognome");
        emailTextField = createTextField("Email");

        // Labels di stato per validazione
        usernameStatusLabel = createStatusLabel();
        passwordStatusLabel = createStatusLabel();
        emailStatusLabel = createStatusLabel();
        confirmPasswordLabel = createStatusLabel();

        // Checkbox per mostrare password
        mostraPasswordCheckBox = new JCheckBox("Mostra password");
        mostraPasswordCheckBox.setBackground(new Color(245, 250, 255));
        mostraPasswordCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Bottoni
        registratiButton = new JButton("Registrati");
        styleButton(registratiButton);
        registratiButton.setEnabled(false); // Disabilitato inizialmente

        backButton = new JButton("Indietro");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setBackground(new Color(180, 70, 70));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setMaximumSize(new Dimension(200, 40));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void setupLayout() {
        // Titolo
        JLabel titleLabel = new JLabel("Crea il tuo Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(70, 130, 180));

        // Pannello per immagine profilo
        JPanel immaginePanel = createImagePanel();

        // Aggiunta componenti
        registerPanel.add(titleLabel);
        registerPanel.add(Box.createVerticalStrut(20));

        registerPanel.add(usernameTextField);
        registerPanel.add(usernameStatusLabel);
        registerPanel.add(Box.createVerticalStrut(10));

        registerPanel.add(passwordPasswordField);
        registerPanel.add(passwordStatusLabel);
        registerPanel.add(Box.createVerticalStrut(5));

        registerPanel.add(ripetiPasswordPasswordField);
        registerPanel.add(confirmPasswordLabel);
        registerPanel.add(Box.createVerticalStrut(5));

        registerPanel.add(mostraPasswordCheckBox);
        registerPanel.add(Box.createVerticalStrut(10));

        registerPanel.add(nomeTextField);
        registerPanel.add(Box.createVerticalStrut(10));

        registerPanel.add(cognomeTextField);
        registerPanel.add(Box.createVerticalStrut(10));

        registerPanel.add(emailTextField);
        registerPanel.add(emailStatusLabel);
        registerPanel.add(Box.createVerticalStrut(10));

        registerPanel.add(immaginePanel);
        registerPanel.add(Box.createVerticalStrut(20));

        registerPanel.add(registratiButton);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(backButton);
    }

    private JPanel createImagePanel() {
        JPanel immaginePanel = new JPanel();
        immaginePanel.setLayout(new BoxLayout(immaginePanel, BoxLayout.X_AXIS));
        immaginePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        immaginePanel.setBackground(new Color(245, 250, 255));
        immaginePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Etichetta
        JLabel imgLabel = new JLabel("Immagine Profilo (opzionale):");
        imgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        imgLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Campo testo
        immagineProfiloTextField = new JTextField();
        immagineProfiloTextField.setEditable(false);
        immagineProfiloTextField.setPreferredSize(new Dimension(200, 30));
        immagineProfiloTextField.setMaximumSize(new Dimension(200, 30));
        immagineProfiloTextField.setText("Nessun file selezionato");
        immagineProfiloTextField.setForeground(Color.GRAY);

        // Bottone
        scegliImmagineButton = new JButton("Scegli File");
        scegliImmagineButton.setPreferredSize(new Dimension(100, 30));
        scegliImmagineButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Aggiunta componenti al pannello principale
        immaginePanel.add(Box.createHorizontalStrut(10));
        immaginePanel.add(imgLabel);
        immaginePanel.add(Box.createHorizontalStrut(10));
        immaginePanel.add(immagineProfiloTextField);
        immaginePanel.add(Box.createHorizontalStrut(10));
        immaginePanel.add(scegliImmagineButton);
        immaginePanel.add(Box.createHorizontalStrut(10));

        return immaginePanel;
    }

    private void setupValidation() {
        // Validazione username
        usernameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateUsername();
                checkFormValidity();
            }
        });

        // Validazione password
        passwordPasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validatePassword();
                checkFormValidity();
            }
        });

        // Validazione conferma password
        ripetiPasswordPasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validatePasswordConfirmation();
                checkFormValidity();
            }
        });

        // Validazione email
        emailTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateEmail();
                checkFormValidity();
            }
        });
    }

    private void setupEventHandlers() {
        // Checkbox mostra password
        mostraPasswordCheckBox.addActionListener(e -> {
            if (mostraPasswordCheckBox.isSelected()) {
                passwordPasswordField.setEchoChar((char) 0);
                ripetiPasswordPasswordField.setEchoChar((char) 0);
            } else {
                passwordPasswordField.setEchoChar('•');
                ripetiPasswordPasswordField.setEchoChar('•');
            }
        });

        // File chooser per immagine
        scegliImmagineButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Immagini", "jpg", "jpeg", "png", "gif"));

            int result = fileChooser.showOpenDialog(registerPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                immagineProfiloTextField.setText(selectedFile.getName());
                immagineProfiloTextField.setForeground(Color.BLACK);
            }
        });

        // Bottone indietro
        backButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    registerPanel,
                    "Sei sicuro di voler tornare indietro? I dati inseriti andranno persi.",
                    "Conferma",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                frame.setContentPane(new Homepage().getHomepagePanel());
                frame.revalidate();
                frame.repaint();
            }
        });

        // Bottone registrazione
        // Bottone registrazione con gestione delle eccezioni
        registratiButton.addActionListener(e -> {
            if (validateAllFields()) {
                try {
                    new UtenteDAO().registrazione(
                            getUsername(),
                            getNome(),
                            getCognome(),
                            getEmail(),
                            getPassword(),
                            getImmagineProfiloPath()
                    );

                    // Torna al login solo se la registrazione è avvenuta con successo
                    frame.setContentPane(new LoginPage(frame).getLoginPanel());
                    frame.revalidate();
                    frame.repaint();

                } catch (SQLException ex) {
                    // Gestisci errori SQL (es. username già esistente, campi obbligatori mancanti)
                    JOptionPane.showMessageDialog(
                            frame,
                            "Errore durante la registrazione: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (ClassNotFoundException ex) {
                    // Gestisci errori di connessione al database
                    JOptionPane.showMessageDialog(
                            frame,
                            "Errore di connessione al database: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    private void validateUsername() {
        String username = usernameTextField.getText().trim();
        if (username.isEmpty()) {
            setStatusLabel(usernameStatusLabel, "Username richiesto", Color.RED);
        } else if (username.length() < 3) {
            setStatusLabel(usernameStatusLabel, "Username troppo corto (min 3 caratteri)", Color.RED);
        } else if (username.length() > 20) {
            setStatusLabel(usernameStatusLabel, "Username troppo lungo (max 20 caratteri)", Color.RED);
        } else {
            setStatusLabel(usernameStatusLabel, "✓ Username valido", new Color(34, 139, 34));
        }
    }

    private void validatePassword() {
        String password = new String(passwordPasswordField.getPassword());
        if (password.isEmpty()) {
            setStatusLabel(passwordStatusLabel, "Password richiesta", Color.RED);
        } else if (password.length() < 6) {
            setStatusLabel(passwordStatusLabel, "Password troppo corta (min 6 caratteri)", Color.RED);
        } else if (!password.matches(".*[A-Z].*")) {
            setStatusLabel(passwordStatusLabel, "Password deve contenere almeno una maiuscola", Color.RED);
        } else if (!password.matches(".*[0-9].*")) {
            setStatusLabel(passwordStatusLabel, "Password deve contenere almeno un numero", Color.RED);
        } else {
            setStatusLabel(passwordStatusLabel, "✓ Password sicura", new Color(34, 139, 34));
        }
    }

    private void validatePasswordConfirmation() {
        String password = new String(passwordPasswordField.getPassword());
        String confirmPassword = new String(ripetiPasswordPasswordField.getPassword());

        if (confirmPassword.isEmpty()) {
            setStatusLabel(confirmPasswordLabel, "Conferma password richiesta", Color.RED);
        } else if (!password.equals(confirmPassword)) {
            setStatusLabel(confirmPasswordLabel, "Le password non coincidono", Color.RED);
        } else {
            setStatusLabel(confirmPasswordLabel, "✓ Password confermata", new Color(34, 139, 34));
        }
    }

    private void validateEmail() {
        String email = emailTextField.getText().trim();
        if (email.isEmpty()) {
            setStatusLabel(emailStatusLabel, "Email richiesta", Color.RED);
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            setStatusLabel(emailStatusLabel, "Formato email non valido", Color.RED);
        } else {
            setStatusLabel(emailStatusLabel, "✓ Email valida", new Color(34, 139, 34));
        }
    }

    private boolean validateAllFields() {
        validateUsername();
        validatePassword();
        validatePasswordConfirmation();
        validateEmail();

        // Verifica campi obbligatori
        boolean isValid = true;

        if (nomeTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(registerPanel, "Il nome è obbligatorio", "Errore", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        if (cognomeTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(registerPanel, "Il cognome è obbligatorio", "Errore", JOptionPane.ERROR_MESSAGE);
            isValid = false;
        }

        return isValid && isFormValid();
    }

    private void checkFormValidity() {
        boolean isValid = isFormValid();
        registratiButton.setEnabled(isValid);

        if (isValid) {
            registratiButton.setBackground(new Color(34, 139, 34));
        } else {
            registratiButton.setBackground(new Color(70, 130, 180));
        }
    }

    private boolean isFormValid() {
        return usernameStatusLabel.getText().startsWith("✓") &&
                passwordStatusLabel.getText().startsWith("✓") &&
                confirmPasswordLabel.getText().startsWith("✓") &&
                emailStatusLabel.getText().startsWith("✓") &&
                !nomeTextField.getText().trim().isEmpty() &&
                !cognomeTextField.getText().trim().isEmpty();
    }

    private JLabel createStatusLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void setStatusLabel(JLabel label, String text, Color color) {
        label.setText(text);
        label.setForeground(color);
    }

    // Metodi helper esistenti
    private JTextField createTextField(String title) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(title));
        return field;
    }

    private JPasswordField createPasswordField(String title) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createTitledBorder(title));
        return field;
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(200, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // Getter methods esistenti
    public JPanel getRegisterPanel() { return registerPanel; }
    public String getUsername() { return usernameTextField.getText().trim(); }
    public String getPassword() { return new String(passwordPasswordField.getPassword()); }
    public String getRipetiPassword() { return new String(ripetiPasswordPasswordField.getPassword()); }
    public String getNome() { return nomeTextField.getText().trim(); }
    public String getCognome() { return cognomeTextField.getText().trim(); }
    public String getEmail() { return emailTextField.getText().trim(); }
    public String getImmagineProfiloPath() { return immagineProfiloTextField.getText(); }
}
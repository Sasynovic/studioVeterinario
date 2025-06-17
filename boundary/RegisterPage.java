package boundary;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.regex.Pattern;
import controller.UtenteController;

public class RegisterPage {
    // Componenti UI
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

    // Etichette di stato
    private JLabel usernameStatusLabel;
    private JLabel passwordStatusLabel;
    private JLabel emailStatusLabel;
    private JLabel confirmPasswordLabel;

    // Riferimenti e utility
    private JFrame frame;
    private Utilities utilities = new Utilities();

    // Gestione immagini
    private JPanel immaginePanel;
    private JLabel previewLabel;
    private static final String DEFAULT_PROFILE_IMAGE = "default.png";
    private File selectedImageFile;
    private static final int PREVIEW_WIDTH = 100;
    private static final int PREVIEW_HEIGHT = 100;

    // Pattern per validazione email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

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

        // Inizializzazione componenti
        usernameTextField = createTextField("Username");
        passwordPasswordField = createPasswordField("Password");
        ripetiPasswordPasswordField = createPasswordField("Ripeti Password");
        nomeTextField = createTextField("Nome");
        cognomeTextField = createTextField("Cognome");
        emailTextField = createTextField("Email");

        usernameStatusLabel = createStatusLabel();
        passwordStatusLabel = createStatusLabel();
        emailStatusLabel = createStatusLabel();
        confirmPasswordLabel = createStatusLabel();

        mostraPasswordCheckBox = new JCheckBox("Mostra password");
        mostraPasswordCheckBox.setBackground(new Color(245, 250, 255));
        mostraPasswordCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        registratiButton = utilities.createButton("Registrati", new Color(70, 130, 180));
        backButton = utilities.createButton("Indietro", new Color(180, 70, 70));
        registratiButton.setEnabled(false);
    }

    private void setupLayout() {
        registerPanel.add(createTitleLabel());
        registerPanel.add(Box.createVerticalStrut(20));

        addFormField(usernameTextField, usernameStatusLabel);
        addFormField(passwordPasswordField, passwordStatusLabel);
        addFormField(ripetiPasswordPasswordField, confirmPasswordLabel);

        registerPanel.add(mostraPasswordCheckBox);
        registerPanel.add(Box.createVerticalStrut(10));

        addFormField(nomeTextField, null);
        addFormField(cognomeTextField, null);
        addFormField(emailTextField, emailStatusLabel);

        registerPanel.add(createImagePanel());
        registerPanel.add(Box.createVerticalStrut(20));

        registerPanel.add(registratiButton);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(backButton);
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Crea il tuo Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(70, 130, 180));
        return titleLabel;
    }

    private void addFormField(JComponent field, JLabel statusLabel) {
        registerPanel.add(field);
        if (statusLabel != null) {
            registerPanel.add(statusLabel);
        }
        registerPanel.add(Box.createVerticalStrut(10));
    }

    private JPanel createImagePanel() {
        immaginePanel = new JPanel();
        immaginePanel.setLayout(new BoxLayout(immaginePanel, BoxLayout.Y_AXIS));
        immaginePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        immaginePanel.setBackground(new Color(245, 250, 255));
        immaginePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Etichetta
        JLabel imgLabel = new JLabel("Immagine Profilo (opzionale):");
        imgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Anteprima immagine
        previewLabel = new JLabel();
        previewLabel.setPreferredSize(new Dimension(PREVIEW_WIDTH, PREVIEW_HEIGHT));
        previewLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadDefaultImage();

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

        // Pannello controlli
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));
        controlsPanel.add(Box.createHorizontalStrut(10));
        controlsPanel.add(immagineProfiloTextField);
        controlsPanel.add(Box.createHorizontalStrut(10));
        controlsPanel.add(scegliImmagineButton);
        controlsPanel.add(Box.createHorizontalStrut(10));

        // Composizione pannello
        immaginePanel.add(imgLabel);
        immaginePanel.add(Box.createVerticalStrut(10));
        immaginePanel.add(previewLabel);
        immaginePanel.add(Box.createVerticalStrut(10));
        immaginePanel.add(controlsPanel);

        return immaginePanel;
    }

    private void loadDefaultImage() {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/propic/" + DEFAULT_PROFILE_IMAGE));
        if (defaultIcon != null) {
            Image img = defaultIcon.getImage().getScaledInstance(
                    PREVIEW_WIDTH, PREVIEW_HEIGHT, Image.SCALE_SMOOTH);
            previewLabel.setIcon(new ImageIcon(img));
        }
    }

    private void setupValidation() {
        addValidationListener(usernameTextField, this::validateUsername);
        addValidationListener(passwordPasswordField, this::validatePassword);
        addValidationListener(ripetiPasswordPasswordField, this::validatePasswordConfirmation);
        addValidationListener(emailTextField, this::validateEmail);
    }

    private void addValidationListener(JComponent component, Runnable validator) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validator.run();
                checkFormValidity();
            }
        });
    }

    private void setupEventHandlers() {
        // Mostra/nascondi password
        mostraPasswordCheckBox.addActionListener(e -> {
            char echoChar = mostraPasswordCheckBox.isSelected() ? (char) 0 : '•';
            passwordPasswordField.setEchoChar(echoChar);
            ripetiPasswordPasswordField.setEchoChar(echoChar);
        });

        // Selezione immagine
        scegliImmagineButton.addActionListener(e -> selectProfileImage());

        // Bottone indietro
        backButton.addActionListener(e -> confirmExit());

        // Bottone registrazione
        registratiButton.addActionListener(e -> registerUser());
    }

    private void selectProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Immagini", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(registerPanel) == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            immagineProfiloTextField.setText(selectedImageFile.getName());
            immagineProfiloTextField.setForeground(Color.BLACK);
            updateImagePreview(selectedImageFile);
        }
    }

    private void updateImagePreview(File imageFile) {
        ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
        Image img = icon.getImage().getScaledInstance(
                PREVIEW_WIDTH, PREVIEW_HEIGHT, Image.SCALE_SMOOTH);
        previewLabel.setIcon(new ImageIcon(img));
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
                registerPanel,
                "Sei sicuro di voler tornare indietro? I dati inseriti andranno persi.",
                "Conferma",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            frame.setContentPane(new Homepage().getHomepagePanel());
            frame.revalidate();
            frame.repaint();
        }
    }

    private void registerUser() {
        if (validateAllFields()) {
            try {
                new UtenteController().addUser(
                        getUsername(),
                        getNome(),
                        getCognome(),
                        getEmail(),
                        getPassword(),
                        getImmagineProfiloPath());

                frame.setContentPane(new LoginPage(frame).getLoginPanel());
                frame.revalidate();
                frame.repaint();
            } catch (SQLException ex) {
                showError("Errore durante la registrazione: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                showError("Errore di connessione al database: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Errore",
                JOptionPane.ERROR_MESSAGE);
    }

    private void validateUsername() {
        String username = usernameTextField.getText().trim();
        if (username.isEmpty()) {
            setStatusLabel(usernameStatusLabel, "Username richiesto", Color.RED);
        } else if (username.length() < 3) {
            setStatusLabel(usernameStatusLabel, "Username troppo corto (min 3 caratteri)", Color.RED);
        } else if (username.length() > 45) {
            setStatusLabel(usernameStatusLabel, "Username troppo lungo (max 45 caratteri)", Color.RED);
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

    // Getter methods esistenti
    public JPanel getRegisterPanel() { return registerPanel; }
    public String getUsername() { return usernameTextField.getText().trim(); }
    public String getPassword() { return new String(passwordPasswordField.getPassword()); }
    public String getNome() { return nomeTextField.getText().trim(); }
    public String getCognome() { return cognomeTextField.getText().trim(); }
    public String getEmail() { return emailTextField.getText().trim(); }

    private String getImmagineProfiloPath() {
        if (selectedImageFile == null || !selectedImageFile.exists()) {
            return DEFAULT_PROFILE_IMAGE;
        }

        File profileDir = new File("images/propic/");
        if (!profileDir.exists()) {
            profileDir.mkdirs();
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        String extension = selectedImageFile.getName().substring(
                selectedImageFile.getName().lastIndexOf("."));
        String newFileName = "img_" + timestamp + extension;

        try {
            File destination = new File(profileDir, newFileName);
            java.nio.file.Files.copy(
                    selectedImageFile.toPath(),
                    destination.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_PROFILE_IMAGE;
        }
    }
}

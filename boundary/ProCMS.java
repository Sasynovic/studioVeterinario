package boundary;

import javax.swing.*;
import java.awt.*;

// importiamo le exception necessarie per la gestione degli errori
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

// solo i controller vanno importanti nella GUI
import controller.AnimaleController;


import entity.Proprietario;
import database.UtenteDAO;

public class ProCMS {
    private JPanel proPanel;
    private JButton logoutButton;
    private JButton effettuaPrenotazioneButton;
    private JButton aggiungiAnimaleButton;
    private JButton modificaProfiloButton;
    private JLabel welcomeLabel;

    public ProCMS(JFrame frame, String nome, String cognome, String username) {
        initializeMainPanel();
        createWelcomeLabel(nome, cognome);
        createAnimalsSection();
        createActionsSection();
        setupEventHandlers(frame, username);
    }

    public JPanel getProPanel() {
        return proPanel;
    }

    private void initializeMainPanel() {
        proPanel = new JPanel();
        proPanel.setBackground(new Color(245, 250, 255));
        proPanel.setLayout(new BoxLayout(proPanel, BoxLayout.Y_AXIS));
        proPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
    }

    //visualizzazione
    private void createWelcomeLabel(String nome, String cognome) {
        welcomeLabel = new JLabel("Benvenuto " + nome + " " + cognome);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        proPanel.add(welcomeLabel);
    }
    //visualizzazione
    private void createAnimalsSection() {
        JPanel animaliPanel = createSectionPanel("Operazioni Principali");
        aggiungiAnimaleButton = createButton("Aggiungi Animale", new Color(70, 130, 180));
        effettuaPrenotazioneButton = createButton("Effettua Prenotazione", new Color(186, 85, 211));

        animaliPanel.add(aggiungiAnimaleButton);
        animaliPanel.add(Box.createVerticalStrut(10));

        animaliPanel.add(effettuaPrenotazioneButton);
        animaliPanel.add(Box.createVerticalStrut(10));

        proPanel.add(animaliPanel);
    }
    //visualizzazione
    private void createActionsSection() {
        JPanel azioniPanel = createSectionPanel("Altre Azioni");
        modificaProfiloButton = createButton("Modifica Profilo", new Color(255, 165, 0));
        logoutButton = createButton("Logout", new Color(169, 169, 169));

        azioniPanel.add(modificaProfiloButton);
        azioniPanel.add(Box.createVerticalStrut(10));
        azioniPanel.add(logoutButton);

        proPanel.add(Box.createVerticalStrut(20));
        proPanel.add(azioniPanel);
    }
    //visualizzazione
    private void setupEventHandlers(JFrame frame, String username) {
        logoutButton.addActionListener(e -> {
            frame.setContentPane(new Homepage().getHomepagePanel());
            frame.revalidate();
            frame.repaint();
        });

        modificaProfiloButton.addActionListener(e -> {
            try {
                new ModificaProfiloDialog(frame, username).setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                showErrorMessage("Errore nell'apertura del dialogo di modifica profilo: " + ex.getMessage());
            }
        });

        aggiungiAnimaleButton.addActionListener(e -> {
            try {
                new inserisciAnimale(frame, username).setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                showErrorMessage("Errore nel'inserimento dell'animale: " + ex.getMessage());
            }
        });


    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(proPanel, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 250, 255));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(100, 100, 100)));
        return panel;
    }

    //visualizzazione
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setMaximumSize(new Dimension(250, 35));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Classe per la modifica del profilo (rimane invariata)
    private static class ModificaProfiloDialog extends JDialog {
        public ModificaProfiloDialog(JFrame parent, String username) throws SQLException, ClassNotFoundException {
            super(parent, "Modifica Profilo", true);
            setSize(500, 400);
            setLocationRelativeTo(parent);

            Proprietario u = new UtenteDAO().getUtenteByUsername(username);
            initializeProfileDialog(u, parent, username);
        }

        private void initializeProfileDialog(Proprietario u, JFrame parent, String originalUsername) {
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(245, 250, 255));

            JPanel fieldsPanel = createFieldsPanel(u);
            JPanel buttonPanel = createButtonPanel(u, parent, originalUsername, fieldsPanel);

            mainPanel.add(fieldsPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            add(mainPanel);
        }

        private JPanel createFieldsPanel(Proprietario u) {
            JPanel fieldsPanel = new JPanel();
            fieldsPanel.setLayout(new GridLayout(6, 2, 10, 10));
            fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            fieldsPanel.setBackground(new Color(245, 250, 255));

            fieldsPanel.add(new JLabel("Username:"));
            fieldsPanel.add(new JTextField(u.getUsername()));
            fieldsPanel.add(new JLabel("Nome:"));
            fieldsPanel.add(new JTextField(u.getNome()));
            fieldsPanel.add(new JLabel("Cognome:"));
            fieldsPanel.add(new JTextField(u.getCognome()));
            fieldsPanel.add(new JLabel("Email:"));
            fieldsPanel.add(new JTextField(u.getEmail()));
            fieldsPanel.add(new JLabel("Password:"));
            fieldsPanel.add(new JPasswordField(u.getPassword()));
            fieldsPanel.add(new JLabel("Immagine Profilo:"));
            fieldsPanel.add(new JTextField(u.getImmagineProfilo()));

            return fieldsPanel;
        }

        private JPanel createButtonPanel(Proprietario u, JFrame parent, String originalUsername, JPanel fieldsPanel) {
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(new Color(245, 250, 255));

            JButton saveButton = createStyledButton("Salva", new Color(70, 130, 180));
            JButton cancelButton = createStyledButton("Annulla", new Color(169, 169, 169));

            saveButton.addActionListener(e -> handleSaveProfile(fieldsPanel, u, parent, originalUsername));
            cancelButton.addActionListener(e -> dispose());

            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            return buttonPanel;
        }

        private JButton createStyledButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            return button;
        }

        private void handleSaveProfile(JPanel fieldsPanel, Proprietario originalUser, JFrame parent, String originalUsername) {
            try {
                Component[] components = fieldsPanel.getComponents();
                String newUsername = ((JTextField) components[1]).getText().trim();
                String newNome = ((JTextField) components[3]).getText().trim();
                String newCognome = ((JTextField) components[5]).getText().trim();
                String newEmail = ((JTextField) components[7]).getText().trim();
                String newPassword = new String(((JPasswordField) components[9]).getPassword()).trim();
                String newImmagine = ((JTextField) components[11]).getText().trim();

                if (newPassword.isEmpty()) {
                    newPassword = null;
                }

                Proprietario updatedUser = new Proprietario(newUsername, newNome, newCognome, newEmail, newPassword, newImmagine);
                UtenteDAO dao = new UtenteDAO();
                dao.aggiornaUtente(updatedUser, originalUsername);

                JOptionPane.showMessageDialog(this, "Profilo aggiornato con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                dispose();

                parent.setContentPane(new ProCMS(parent, newNome, newCognome, newUsername).getProPanel());
                parent.revalidate();
                parent.repaint();

            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento del profilo: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Dialog per inserire un animale
    private static class inserisciAnimale extends JDialog{
        public inserisciAnimale(JFrame parente, String usernameProprietario) throws SQLException, ClassNotFoundException {
            super(parente, "Inserisci Animale", true);
            setSize(500, 400);
            setLocationRelativeTo(parente);
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(new Color(245, 250, 255));

            // Campi per l'inserimento
            mainPanel.add(new JLabel("Chip:"));
            JTextField chipField = new JTextField();
            mainPanel.add(chipField);

            mainPanel.add(new JLabel("Nome:"));
            JTextField nomeField = new JTextField();
            mainPanel.add(nomeField);

            mainPanel.add(new JLabel("Tipo:"));
            JComboBox<String> tipoCombo = new JComboBox<>(new String[]{
                    "Cane",
                    "Gatto",
                    "Coniglio",
                    "Criceto",
                    "Uccello",
                    "Pesce",
                    "Tartaruga",
                    "Furetto",
                    "Porcellino d'India",
                    "Ratto",
                    "Serpente",
                    "Cavallo",
                    "Altro"
            });
            tipoCombo.setSelectedIndex(0);
            mainPanel.add(tipoCombo);

            mainPanel.add(new JLabel("Razza:"));
            JTextField razzaField = new JTextField();
            mainPanel.add(razzaField);

            mainPanel.add(new JLabel("Colore:"));
            JTextField coloreField = new JTextField();
            mainPanel.add(coloreField);

            mainPanel.add(new JLabel("Data di Nascita (yyyy/MM/gg):"));
            JTextField dataNascitaField = new JTextField();
            mainPanel.add(dataNascitaField);

            add(mainPanel, BorderLayout.CENTER);
            // Pannello dei pulsanti

            JButton salvaButton = new JButton("Salva");
            salvaButton.setBackground(new Color(70, 130, 180));
            salvaButton.setForeground(Color.WHITE);
            salvaButton.setFocusPainted(false);
            salvaButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            salvaButton.setMaximumSize(new Dimension(100, 35));
            salvaButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            salvaButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


            JButton annullaButton = new JButton("Annulla");
            annullaButton.setBackground(new Color(169, 169, 169));
            annullaButton.setForeground(Color.WHITE);
            annullaButton.setFocusPainted(false);
            annullaButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            annullaButton.setMaximumSize(new Dimension(100, 35));
            annullaButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            annullaButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            mainPanel.add(annullaButton);
            mainPanel.add(salvaButton);

            salvaButton.addActionListener(e -> {
                    // Estrai e valida i dati
                    String chipText = chipField.getText().trim();
                    String nome = nomeField.getText().trim();
                    Object selectedItem = tipoCombo.getSelectedItem();
                    String tipo = (selectedItem != null) ? selectedItem.toString() : "";
                    String razza = razzaField.getText().trim();
                    String colore = coloreField.getText().trim();
                    String dataNascitaText = dataNascitaField.getText().trim();

                    // Controlla campi vuoti
                    if (chipText.isEmpty() || nome.isEmpty() || tipo.isEmpty() || razza.isEmpty() || colore.isEmpty() || dataNascitaText.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tutti i campi sono obbligatori.");
                        return;
                    }

                    int chip;
                    try {
                        chip = Integer.parseInt(chipText);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Il chip deve essere un numero intero.");
                        return;
                    }

                    Date dataNascita;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        sdf.setLenient(false);
                        dataNascita = sdf.parse(dataNascitaText);
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(this, "Formato data non valido. Usa aaaa/MM/gg.");
                        return;
                    }

                    // Salvataggio
                    AnimaleController animaleController = new AnimaleController();
                    try {
                        animaleController.addAnimale(chip, nome, tipo, razza, colore, dataNascita, usernameProprietario);
                        JOptionPane.showMessageDialog(this, "Animale inserito con successo.");
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + ex.getMessage());
                    }
                });
            annullaButton.addActionListener(e -> dispose());
        }
    }

}
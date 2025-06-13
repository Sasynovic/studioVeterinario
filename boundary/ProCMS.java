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

public class ProCMS {
    private JPanel proPanel;
    private JButton logoutButton;
    private JButton effettuaPrenotazioneButton;
    private JButton aggiungiAnimaleButton;
    private JButton modificaProfiloButton;
    private JLabel welcomeLabel;

    private static Utilities utilities = new Utilities();

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
        JPanel animaliPanel = utilities.createSectionPanel("Operazioni Principali");
        aggiungiAnimaleButton = utilities.createButton("Aggiungi Animale", utilities.Blue);
        effettuaPrenotazioneButton = utilities.createButton("Effettua Prenotazione",utilities.Green);

        animaliPanel.add(aggiungiAnimaleButton);
        animaliPanel.add(Box.createVerticalStrut(10));

        animaliPanel.add(effettuaPrenotazioneButton);
        animaliPanel.add(Box.createVerticalStrut(10));

        proPanel.add(animaliPanel);
    }
    //visualizzazione
    private void createActionsSection() {
        JPanel azioniPanel = utilities.createSectionPanel("Altre Azioni");
        modificaProfiloButton = utilities.createButton("Modifica Profilo", utilities.Orange);
        logoutButton = utilities.createButton("Logout", utilities.DarkGray);

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
//
//        modificaProfiloButton.addActionListener(e -> {
//            try {
//                new ModificaProfiloDialog(frame, username).setVisible(true);
//            } catch (SQLException | ClassNotFoundException ex) {
//                showErrorMessage("Errore nell'apertura del dialogo di modifica profilo: " + ex.getMessage());
//            }
//        });

        aggiungiAnimaleButton.addActionListener(e -> {
            try {
                new inserisciAnimale(frame, username).setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                utilities.showErrorMessage(proPanel,"Errore nel'inserimento dell'animale: " + ex.getMessage());
            }
        });


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

            JButton salvaButton = utilities.createButton(("Salva"), new Color(70, 130, 180));
            JButton annullaButton = utilities.createButton(("Anulla"), new Color(159, 0, 0));

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
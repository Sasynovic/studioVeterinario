package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import controller.PrenotazioneController;
import entity.Agenda;
import controller.AgendaController;

public class AdminCMS {
    private JPanel adminPanel;
    private JButton elencoVisiteGiornaliereButton;
    private JButton visualizzaAnimaliUltimaVaccinazioneButton;
    private JButton inserisciDisponibilitaButton;
    private JButton logoutButton;
    private JLabel titleLabel;

    protected static Utilities utilities = new Utilities();

    private static String formatDataItaliana() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ITALIAN);
        return sdf.format(new Date());
    }

    public AdminCMS(JFrame frame) {
        initializeMainPanel();
        createWelcomeLabel();
        createMainActionsSection();
        createOtherActionsSection();
        setupEventHandlers(frame);
    }

    public JPanel getAdminPanel() {
        return adminPanel;
    }

    // Setup base del pannello
    private void initializeMainPanel() {
        adminPanel = new JPanel();
        adminPanel.setBackground(new Color(245, 250, 255));
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        adminPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
    }

    // Titolo/benvenuto
    private void createWelcomeLabel() {
        titleLabel = new JLabel("Amministrazione CMS");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        adminPanel.add(titleLabel);
    }

    // Sezione con i pulsanti principali
    private void createMainActionsSection() {
        JPanel mainActionsPanel = utilities.createSectionPanel("Operazioni Amministrative");

        elencoVisiteGiornaliereButton = utilities.createButton("Elenco Visite Giornaliere", utilities.Blue);
        visualizzaAnimaliUltimaVaccinazioneButton = utilities.createButton("Visualizza Animali Ultima Vaccinazione", utilities.Blue);
        inserisciDisponibilitaButton = utilities.createButton("Inserisci Disponibilità", utilities.Green);

        mainActionsPanel.add(elencoVisiteGiornaliereButton);
        mainActionsPanel.add(Box.createVerticalStrut(10));
        mainActionsPanel.add(visualizzaAnimaliUltimaVaccinazioneButton);
        mainActionsPanel.add(Box.createVerticalStrut(10));
        mainActionsPanel.add(inserisciDisponibilitaButton);

        adminPanel.add(mainActionsPanel);
    }

    // Sezione con il logout
    private void createOtherActionsSection() {
        JPanel otherActionsPanel = utilities.createSectionPanel("Altre Azioni");

        logoutButton = utilities.createButton("Logout", utilities.Red);
        otherActionsPanel.add(logoutButton);

        adminPanel.add(Box.createVerticalStrut(20));
        adminPanel.add(otherActionsPanel);
    }

    // Assegnazione listener ai bottoni
    private void setupEventHandlers(JFrame frame) {
        logoutButton.addActionListener(e -> {
            frame.setContentPane(new Homepage().getHomepagePanel());
            frame.revalidate();
            frame.repaint();
        });

        elencoVisiteGiornaliereButton.addActionListener(e -> {
            new mostraPrenotazioniDialog(frame).setVisible(true);
        });

        visualizzaAnimaliUltimaVaccinazioneButton.addActionListener(e -> {
            new visualizzaUltimaVaccinazioneDialog(frame).setVisible(true);
        });

        inserisciDisponibilitaButton.addActionListener(e -> {
            new InserisciDisponibilitaDialog(frame).setVisible(true);
        });
    }

    private static class mostraPrenotazioniDialog extends JDialog {
        public mostraPrenotazioniDialog(JFrame parente) {
            // super() DEVE essere la prima istruzione
            super(parente, "Visite del giorno " + formatDataItaliana(), true);

            Date oggi = new Date();

            // Titolo e pannello principale
            JPanel contentPanel = utilities.createSectionPanel("Visite del giorno");
            contentPanel.setLayout(new BorderLayout());

            // Recupera le prenotazioni del giorno
            List<Agenda> pDay;
            try {
                AgendaController ac = new AgendaController();
                pDay = ac.getVisiteDay(oggi);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore durante il caricamento delle prenotazioni: " + ex.getMessage());
                dispose();
                return;
            }

            // Tabella o lista
            String[] colonne = {"Orario", "Stato", "ChipAnimale", "Animale", "Proprietario"};
            DefaultTableModel model = new DefaultTableModel(colonne, 0);

            for (Agenda a : pDay) {
                int orarioInt = a.getOrario();
                String orarioFormattato = String.format("%02d:00", orarioInt);

                model.addRow(new Object[]{
                        orarioFormattato,
                        a.getNomeStato(),
                        a.getChipAnimale(),
                        a.getNomeAnimale(),
                        a.getNomeProprietario()
                });
            }

            JTable tabella = new JTable(model);
            tabella.setEnabled(false); // sola lettura
            JScrollPane scrollPane = new JScrollPane(tabella);
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            // Pannello pulsante chiudi
            JButton chiudiButton = utilities.createButton("Chiudi", utilities.Red);
            chiudiButton.addActionListener(e -> dispose());
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(chiudiButton);

            // Layout complessivo
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(contentPanel, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(parente);
        }


    }
    private static class visualizzaUltimaVaccinazioneDialog extends JDialog {
        public visualizzaUltimaVaccinazioneDialog(JFrame parente){
            super(parente, "Vaccinazioni distanti almeno un anno da : " + formatDataItaliana(), true);

            Date oggi = new Date();

            // Titolo e pannello principale
            JPanel contentPanel = utilities.createSectionPanel("Vaccinazioni distanti almeno un anno da oggi");
            contentPanel.setLayout(new BorderLayout());

            // Recupera le prenotazioni del giorno
            List<Agenda> pDay;
            try {
                AgendaController ac = new AgendaController();
                pDay = ac.getVaccinazioniYear(oggi);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore durante il caricamento delle prenotazioni: " + ex.getMessage());
                dispose();
                return;
            }

            // Tabella o lista
            String[] colonne = {"Data", "Orario", "ChipAnimale", "Animale", "Proprietario"};
            DefaultTableModel model = new DefaultTableModel(colonne, 0);

            for (Agenda a : pDay) {
                int orarioInt = a.getOrario();
                String orarioFormattato = String.format("%02d:00", orarioInt);

                model.addRow(new Object[]{
                        a.getData(),
                        orarioFormattato,
                        a.getChipAnimale(),
                        a.getNomeAnimale(),
                        a.getNomeProprietario()
                });
            }

            JTable tabella = new JTable(model);
            tabella.setEnabled(false); // sola lettura
            JScrollPane scrollPane = new JScrollPane(tabella);
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            // Pannello pulsante chiudi
            JButton chiudiButton = utilities.createButton("Chiudi", utilities.Red);
            chiudiButton.addActionListener(e -> dispose());
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(chiudiButton);

            // Layout complessivo
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(contentPanel, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(parente);
        }

        }

    private static class InserisciDisponibilitaDialog extends JDialog {
        private JTextField dataField;
        private JComboBox<String> orarioCombo; // Dichiarata come campo della classe
        private JButton confermaButton;
        private JButton annullaButton;

        public InserisciDisponibilitaDialog(JFrame parente) {
            super(parente, "Inserisci disponibilita", true);

            // Titolo e pannello principale
            JPanel contentPanel = utilities.createSectionPanel("Inserisci disponibilita");
            contentPanel.setLayout(new GridLayout(3, 2, 10, 10));

            // Campi input
            contentPanel.add(new JLabel("Data (YYYY-MM-DD):"));
            dataField = new JTextField();
            contentPanel.add(dataField);

            contentPanel.add(new JLabel("Orario (HH:MM):"));
            orarioCombo = new JComboBox<>(); // Inizializzata correttamente
            for (int ora = 8; ora <= 17; ora++) {
                orarioCombo.addItem(String.format("%02d:00", ora));
            }
            contentPanel.add(orarioCombo);

            // Bottoni
            confermaButton = utilities.createButton("Conferma", utilities.Green);
            annullaButton = utilities.createButton("Annulla", utilities.Red);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(confermaButton);
            buttonPanel.add(annullaButton);

            // Aggiunta ai pannelli della dialog
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(contentPanel, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            pack();
            setLocationRelativeTo(parente);

            // Azioni
            annullaButton.addActionListener(e -> dispose());

            confermaButton.addActionListener(e -> {
                PrenotazioneController pc = new PrenotazioneController();
                try {
                    String dataInputString = dataField.getText().trim();
                    int orario = orarioCombo.getSelectedIndex() + 8; // 8 è l'ora di inizio

                    // Validazione data vuota
                    if (dataInputString.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Compila il campo data con un valore valido.");
                        return;
                    }

                    // Validazione formato data
                    Date dataInput = validaEConvertiData(dataInputString);
                    if (dataInput == null) {
                        JOptionPane.showMessageDialog(this, "Formato data non valido. Usa il formato YYYY-MM-DD (es: 2025-06-16).");
                        return;
                    }

                    // Validazione data nel passato
                    Date oggi = new Date();
                    if (dataInput.before(oggi)) {
                        JOptionPane.showMessageDialog(this, "Non puoi inserire una data nel passato.");
                        return;
                    }

                    pc.inserisciDisponibilita(dataInput, orario, 0);
                    JOptionPane.showMessageDialog(this, "Disponibilità inserita con successo.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante l'inserimento della disponibilità: " + ex.getMessage());
                }
            });
        }

        /**
         * Valida e converte una stringa in formato YYYY-MM-DD in un oggetto Date
         * @param dataString la stringa da validare
         * @return Date se valida, null se non valida
         */
        private Date validaEConvertiData(String dataString) {
            try {
                // Pattern per validare il formato YYYY-MM-DD
                if (!dataString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    return null;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false); // Non accetta date invalide come 2025-02-30

                Date data = sdf.parse(dataString);

                // Controlla che l'anno sia tra 2025 e 2030
                Calendar cal = Calendar.getInstance();
                cal.setTime(data);
                int anno = cal.get(Calendar.YEAR);

                if (anno < 2025 || anno > 2030) {
                    return null;
                }

                return data;
            } catch (ParseException e) {
                return null;
            }
        }
    }
}

// Metodo helper per formattare la data

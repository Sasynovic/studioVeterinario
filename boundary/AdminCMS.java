package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


import java.text.SimpleDateFormat;
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

        elencoVisiteGiornaliereButton.addActionListener(e -> new MostraVisiteDialog(frame).setVisible(true));

        visualizzaAnimaliUltimaVaccinazioneButton.addActionListener(e -> new VisualizzaUltimaVaccinazioneDialog(frame).setVisible(true));

        inserisciDisponibilitaButton.addActionListener(e -> new InserisciDisponibilitaDialog(frame).setVisible(true));
    }

    private static class MostraVisiteDialog extends JDialog {
        public MostraVisiteDialog(JFrame parente) {
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
    private static class VisualizzaUltimaVaccinazioneDialog extends JDialog {
        public VisualizzaUltimaVaccinazioneDialog(JFrame parente){
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
        private Date dataSelezionata;
        private JLabel dataSelezionataLabel;
        private JComboBox<String> orarioCombo;
        private JButton confermaButton;

        public InserisciDisponibilitaDialog(JFrame parente) {
            super(parente, "Inserisci disponibilità", true);

            JPanel mainPanel = utilities.createSectionPanel("Inserisci disponibilità");
            mainPanel.setLayout(new BorderLayout(10, 10));

            AgendaController ac = new AgendaController();

            // Pannello per la selezione della data con il calendario
            JPanel dataPanel = new JPanel(new BorderLayout());
            dataSelezionataLabel = new JLabel("Nessuna data selezionata", JLabel.CENTER);
            dataSelezionataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            dataSelezionataLabel.setForeground(new Color(70, 130, 180));

            // Aggiungi il calendario avanzato con callback
            JPanel calendarioPanel = utilities.createAdvancedCalendar(dataSelezionataLabel, selectedDate -> {
                dataSelezionata = selectedDate;
                aggiornaOrariDisponibili(ac);
            });

            // Limita l'altezza del calendario
            calendarioPanel.setPreferredSize(new Dimension(calendarioPanel.getPreferredSize().width, 300));
            calendarioPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

            dataPanel.add(calendarioPanel, BorderLayout.CENTER);
            dataPanel.add(dataSelezionataLabel, BorderLayout.SOUTH);

            // Pannello per la selezione dell'orario con più spazio
            JPanel orarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            orarioPanel.setBorder(BorderFactory.createTitledBorder("Seleziona Orario"));
            orarioPanel.setPreferredSize(new Dimension(550, 60));

            JLabel orarioLabel = new JLabel("Orario:");
            orarioLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            orarioPanel.add(orarioLabel);

            orarioCombo = new JComboBox<>();
            orarioCombo.setPreferredSize(new Dimension(120, 25));
            orarioCombo.addItem("Seleziona prima una data"); // Placeholder iniziale
            orarioCombo.setEnabled(false); // Disabilitato fino alla selezione della data
            orarioPanel.add(orarioCombo);

            // Pannello pulsanti
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setPreferredSize(new Dimension(550, 50));
            confermaButton = utilities.createButton("Conferma", utilities.Green);
            confermaButton.setEnabled(false); // Disabilitato inizialmente
            JButton annullaButton = utilities.createButton("Annulla", utilities.Red);
            buttonPanel.add(confermaButton);
            buttonPanel.add(annullaButton);

            // Aggiungi tutto al pannello principale con BoxLayout per controllo migliore
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(dataPanel);
            mainPanel.add(Box.createVerticalStrut(10)); // Spazio tra i componenti
            mainPanel.add(orarioPanel);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(buttonPanel);

            getContentPane().add(mainPanel);
            setSize(650, 550); // Dimensione leggermente più grande
            setMinimumSize(new Dimension(600, 500)); // Dimensione minima
            setLocationRelativeTo(parente);

            // Forza il repaint dopo la creazione
            SwingUtilities.invokeLater(() -> {
                validate();
                repaint();
            });

            // Listener per i pulsanti
            annullaButton.addActionListener(e -> dispose());

            confermaButton.addActionListener(e -> {
                PrenotazioneController pc = new PrenotazioneController();
                try {
                    // Validazione data
                    if (dataSelezionata == null) {
                        JOptionPane.showMessageDialog(this, "Seleziona una data dal calendario.");
                        return;
                    }

                    // Validazione data nel passato
                    Date oggi = new Date();
                    if (dataSelezionata.before(oggi)) {
                        JOptionPane.showMessageDialog(this, "Non puoi inserire una data nel passato.");
                        return;
                    }

                    // Validazione orario migliorata
                    String orarioString = (String) orarioCombo.getSelectedItem();
                    if (orarioString == null || orarioString.equals("Seleziona prima una data") ||
                            orarioString.equals("Nessun orario disponibile")) {
                        JOptionPane.showMessageDialog(this, "Seleziona un orario disponibile.");
                        return;
                    }

                    // Debug: stampa l'orario selezionato
                    System.out.println("Orario selezionato: " + orarioString);

                    int orario = Integer.parseInt(orarioString.split(":")[0]);

                    pc.inserisciDisponibilita(dataSelezionata, orario, 0);
                    JOptionPane.showMessageDialog(this, "Disponibilità inserita con successo.");
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Formato orario non valido: " + ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante l'inserimento della disponibilità: " + ex.getMessage());
                    ex.printStackTrace(); // Per debug
                }
            });
        }

        /**
         * Aggiorna la lista degli orari disponibili per la data selezionata
         */
        private void aggiornaOrariDisponibili(AgendaController ac) {
            if (dataSelezionata == null) {
                orarioCombo.removeAllItems();
                orarioCombo.addItem("Seleziona prima una data");
                orarioCombo.setEnabled(false);
                confermaButton.setEnabled(false);
                return;
            }

            try {
                List<Agenda> orariDisponibili = ac.getOrariDisponibili(dataSelezionata);

                // Rimuovi SwingUtilities.invokeLater per evitare problemi di concorrenza
                orarioCombo.removeAllItems();

                if (orariDisponibili.isEmpty()) {
                    orarioCombo.addItem("Nessun orario disponibile");
                    orarioCombo.setEnabled(false);
                    confermaButton.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Nessun orario disponibile per la data selezionata.");
                } else {
                    orarioCombo.setEnabled(true);
                    confermaButton.setEnabled(true);

                    // Debug: stampa gli orari trovati
                    System.out.println("Orari disponibili trovati: " + orariDisponibili.size());

                    for (Agenda orario : orariDisponibili) {
                        String orarioFormatted = String.format("%02d:00", orario.getOrario());
                        orarioCombo.addItem(orarioFormatted);
                        System.out.println("Aggiunto orario: " + orarioFormatted); // Debug
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore durante il recupero degli orari: " + ex.getMessage());
                ex.printStackTrace(); // Per debug
            }
        }
    }}
package boundary;

//import controller.PrenotazioneController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import entity.Agenda;
import controller.AgendaController;

public class VetCMS {
    private JPanel vetPanel;
    private JButton elencoPrenotazioniGiornaliereButton;
    private JButton registraVisitaButton;
    private JButton logoutButton;
    private JLabel titleLabel;

    protected static Utilities utilities = new Utilities();

    private static String formatDataItaliana() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ITALIAN);
        return sdf.format(new Date());
    }

    public VetCMS(JFrame frame) {
        initializeMainPanel();
        createWelcomeLabel();
        createMainActionsSection();
        createOtherActionsSection();
        setupEventHandlers(frame);
    }

    public JPanel getvetPanel() {
        return vetPanel;
    }

    // Setup base del pannello
    private void initializeMainPanel() {
        vetPanel = new JPanel();
        vetPanel.setBackground(new Color(245, 250, 255));
        vetPanel.setLayout(new BoxLayout(vetPanel, BoxLayout.Y_AXIS));
        vetPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
    }

    // Titolo/benvenuto
    private void createWelcomeLabel() {
        titleLabel = new JLabel("Veterinario CMS");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        vetPanel.add(titleLabel);
    }

    // Sezione con i pulsanti principali
    private void createMainActionsSection() {
        JPanel mainActionsPanel = utilities.createSectionPanel("Operazioni Amministrative");

        elencoPrenotazioniGiornaliereButton = utilities.createButton("Elenco prenotazioni Giornaliere", utilities.Blue);
        registraVisitaButton = utilities.createButton("Registra visita", utilities.Blue);

        mainActionsPanel.add(elencoPrenotazioniGiornaliereButton);
        mainActionsPanel.add(Box.createVerticalStrut(10));
        mainActionsPanel.add(registraVisitaButton);

        vetPanel.add(mainActionsPanel);
    }

    // Sezione con il logout
    private void createOtherActionsSection() {
        JPanel otherActionsPanel = utilities.createSectionPanel("Altre Azioni");

        logoutButton = utilities.createButton("Logout", utilities.Red);
        otherActionsPanel.add(logoutButton);

        vetPanel.add(Box.createVerticalStrut(20));
        vetPanel.add(otherActionsPanel);
    }

    // Assegnazione listener ai bottoni
    private void setupEventHandlers(JFrame frame) {
        logoutButton.addActionListener(e -> {
            frame.setContentPane(new Homepage().getHomepagePanel());
            frame.revalidate();
            frame.repaint();
        });

        elencoPrenotazioniGiornaliereButton.addActionListener(e -> {
            new mostraPrenotazioniDialog(frame).setVisible(true);
        });

        registraVisitaButton.addActionListener(e -> {
            new registraVisita(frame).setVisible(true);
        });
    }

    private static class mostraPrenotazioniDialog extends JDialog {
        public mostraPrenotazioniDialog(JFrame parente) {
            // super() DEVE essere la prima istruzione
            super(parente, "Prenotazioni del giorno " + formatDataItaliana(), true);

            Date oggi = new Date();

            // Titolo e pannello principale
            JPanel contentPanel = utilities.createSectionPanel("Prenotazioni del giorno");
            contentPanel.setLayout(new BorderLayout());

            // Recupera le prenotazioni del giorno
            List<Agenda> pDay;
            try {
                AgendaController ac = new AgendaController();
                pDay = ac.getPrenotazioniDay(oggi);
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

    private static class registraVisita extends JDialog {
        public registraVisita(JFrame parente) {
            // super() DEVE essere la prima istruzione
            super(parente, "Registra Visita", true);

            // Titolo e pannello principale
            JPanel contentPanel = utilities.createSectionPanel("Registra Visita");
            contentPanel.setLayout(new BorderLayout());

            // Form per inserire i dati della visita
            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JComboBox<String> giornoPrenotazione = new JComboBox<>();

            // in base a al giorno selezionato, si popola il combo con gli orari presenti

            // combo per selezionare il tipo della visita
            JComboBox<String> tipoVisita = new JComboBox<>(utilities.visitType);

            JLabel chipAnimaleLabel = new JLabel("Descrizione visita:");
            JTextField chipAnimaleField = new JTextField();
            formPanel.add(chipAnimaleLabel);
            formPanel.add(chipAnimaleField);

            JLabel costoVisita = new JLabel("Costo visita:");
            JTextField costoVisitaField = new JTextField();
            formPanel.add(costoVisita);

            JButton registraButton = utilities.createButton("Registra Visita", utilities.Blue);

            registraButton.addActionListener(e -> {
                try {
                    AgendaController ac = new AgendaController();

                    JOptionPane.showMessageDialog(this, "Visita registrata con successo!");
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Orario e Chip Animale devono essere numeri.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante la registrazione della visita: " + ex.getMessage());
                }

            });
        }
    }
}

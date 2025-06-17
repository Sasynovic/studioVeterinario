package boundary;

//import controller.PrenotazioneController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import controller.FarmacoController;
import controller.PrenotazioneController;
import controller.VisitaController;
import entity.Agenda;
import controller.AgendaController;
import entity.Farmaco;

public class VetCMS {
    private final String usernameVet;
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

    public VetCMS(JFrame frame, String usernameVet) {
        this.usernameVet = usernameVet;
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
            new MostraPrenotazioniDialog(frame).setVisible(true);
        });

        registraVisitaButton.addActionListener(e -> {
            new RegistraVisita(frame, usernameVet).setVisible(true);
        });
    }

    private static class MostraPrenotazioniDialog extends JDialog {
        public MostraPrenotazioniDialog(JFrame parente) {
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

    private static class RegistraVisita extends JDialog {
        private List<Integer> farmaciSelezionati = new ArrayList<>();
        private DefaultListModel<String> farmaciSelezionatiModel = new DefaultListModel<>();
        private FarmacoController farmacoController = new FarmacoController();
        private String usernameVet; // Aggiunto campo per username veterinario

        public RegistraVisita(JFrame parente, String usernameVet) {
            super(parente, "Registra Visita", true);
            this.usernameVet = usernameVet;

            // Titolo e pannello principale
            JPanel contentPanel = utilities.createSectionPanel("Registra Visita");
            contentPanel.setLayout(new BorderLayout());

            // Form per inserire i dati della visita
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            Date oggi = new Date();
            AgendaController ac = new AgendaController();
            List<Agenda> prenotazioniDisponibili = ac.getPrenotazioniBeforeDay(oggi);

            // Sezione prenotazione e tipo visita
            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            JComboBox<String> prenotazioniCombo = new JComboBox<>();
            prenotazioniDisponibili.forEach(prenotazione ->
                    prenotazioniCombo.addItem(prenotazione.getData() + " " +
                            String.format("%02d:00", prenotazione.getOrario()) + " - " +
                            prenotazione.getNomeAnimale() + " (" + prenotazione.getNomeProprietario() + ")")
            );

            JComboBox<String> tipoVisita = new JComboBox<>(utilities.visitType);

            infoPanel.add(new JLabel("Prenotazione:"));
            infoPanel.add(prenotazioniCombo);
            infoPanel.add(new JLabel("Tipo Visita:"));
            infoPanel.add(tipoVisita);
            formPanel.add(infoPanel);

            // Sezione descrizione
            JPanel descrizionePanel = new JPanel(new BorderLayout());
            descrizionePanel.add(new JLabel("Descrizione visita:"), BorderLayout.NORTH);
            JTextArea descrizioneArea = new JTextArea(3, 20);
            descrizioneArea.setLineWrap(true);
            descrizionePanel.add(new JScrollPane(descrizioneArea), BorderLayout.CENTER);
            formPanel.add(Box.createVerticalStrut(10));
            formPanel.add(descrizionePanel);

            // Stato visita
            JPanel statoPanel = new JPanel(new BorderLayout());
            statoPanel.add(new JLabel("Stato visita:"), BorderLayout.NORTH);
            JComboBox<String> statoVisitaCombo = new JComboBox<>(new String[]{"Annullato", "Confermata", "Terminata"});
            statoPanel.add(statoVisitaCombo, BorderLayout.CENTER);
            formPanel.add(Box.createVerticalStrut(10));
            formPanel.add(statoPanel);

            // Sezione costo visita (corretta)
            JPanel costoPanel = new JPanel(new BorderLayout());
            costoPanel.add(new JLabel("Costo visita:"), BorderLayout.NORTH);
            JTextField costoField = new JTextField("0.00");
            costoPanel.add(costoField, BorderLayout.CENTER);
            formPanel.add(Box.createVerticalStrut(10));
            formPanel.add(costoPanel);

            // Sezione farmaci selezionati
            JPanel farmaciPanel = new JPanel(new BorderLayout());
            farmaciPanel.add(new JLabel("Farmaci selezionati:"), BorderLayout.NORTH);
            JList<String> farmaciList = new JList<>(farmaciSelezionatiModel);
            farmaciPanel.add(new JScrollPane(farmaciList), BorderLayout.CENTER);

            // Pulsante per rimuovere farmaci selezionati
            JButton rimuoviFarmacoButton = utilities.createButton("Rimuovi selezionato", utilities.Red);
            rimuoviFarmacoButton.addActionListener(e -> {
                int selectedIndex = farmaciList.getSelectedIndex();
                if (selectedIndex != -1) {
                    farmaciSelezionati.remove(selectedIndex);
                    farmaciSelezionatiModel.remove(selectedIndex);
                }
            });

            JPanel farmaciButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            farmaciButtonPanel.add(rimuoviFarmacoButton);
            farmaciPanel.add(farmaciButtonPanel, BorderLayout.SOUTH);

            formPanel.add(Box.createVerticalStrut(10));
            formPanel.add(farmaciPanel);

            // Pulsanti azione
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton aggiungiFarmacoButton = utilities.createButton("Aggiungi Farmaco", utilities.Blue);
            JButton registraButton = utilities.createButton("Registra Visita", utilities.Green);

            aggiungiFarmacoButton.addActionListener(e -> mostraDialogoSelezioneFarmaco());
            actionPanel.add(aggiungiFarmacoButton);
            actionPanel.add(registraButton);

            contentPanel.add(formPanel, BorderLayout.CENTER);
            contentPanel.add(actionPanel, BorderLayout.SOUTH);

            registraButton.addActionListener(e -> registraVisitaAction(
                    prenotazioniCombo,
                    tipoVisita,
                    descrizioneArea,
                    costoField,
                    statoVisitaCombo
            ));

            getContentPane().add(contentPanel);
            pack();
            setLocationRelativeTo(parente);
        }

        private void registraVisitaAction(JComboBox<String> prenotazioniCombo,
                                          JComboBox<String> tipoVisita,
                                          JTextArea descrizioneArea,
                                          JTextField costoField,
                                          JComboBox<String> statoVisitaCombo) {
            try {
                String prenotazioneSelezionata = (String) prenotazioniCombo.getSelectedItem();
                if (prenotazioneSelezionata == null || prenotazioneSelezionata.isEmpty()) {
                    throw new IllegalArgumentException("Selezionare una prenotazione valida");
                }

                // Estrazione dati prenotazione
                String[] parts = prenotazioneSelezionata.split(" - ");
                String dataOrario = parts[0];
                String[] dataOrarioParts = dataOrario.split(" ");
                String data = dataOrarioParts[0];
                int orario = Integer.parseInt(dataOrarioParts[1].replace(":00", ""));

                System.out.println("Data: " + data + ", Orario: " + orario);

                // Recupero altri dati
                String tipo = (String) tipoVisita.getSelectedItem();
                String descrizione = descrizioneArea.getText();
                double costo;
                try {
                    costo = Double.parseDouble(costoField.getText().replace(",", "."));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Formato costo non valido");
                }
                String statoVisita = (String) statoVisitaCombo.getSelectedItem();

                if (costo < 0) {
                    throw new IllegalArgumentException("Il costo non può essere negativo");
                }

                // Controllo campi obbligatori
                if (descrizione.isEmpty()) {
                    throw new IllegalArgumentException("La descrizione è obbligatoria");
                }

                // Converti la stringa data in Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dataDate;
                try {
                    dataDate = sdf.parse(data);
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Formato data non valido: " + data);
                }

                // Converti lo stato visita in codice numerico
                int statoVisitaCode = switch (statoVisita) {
                    case "Annullato" -> 2;
                    case "Confermata" -> 3;
                    case "Terminata" -> 4;
                    default -> throw new IllegalArgumentException("Stato visita non valido: " + statoVisita);
                };

                // Inserimento visita
                VisitaController vc = new VisitaController();
                int idVisita = vc.inserisciVisita(tipo, descrizione, costo, usernameVet);

                if (idVisita < 0) {
                    throw new Exception("Errore durante l'inserimento della visita");
                }

                // Collegamento farmaci
                for (Integer idFarmaco : farmaciSelezionati) {
                    farmacoController.impiegaFarmaco(idVisita, idFarmaco);
                }

                // Aggiornamento prenotazione
                PrenotazioneController pc = new PrenotazioneController();
                pc.updatePrenotazioneVet(dataDate, orario, idVisita, statoVisitaCode);

                JOptionPane.showMessageDialog(this, "Visita registrata con successo!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Errore durante la registrazione: " + ex.getMessage(),
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void mostraDialogoSelezioneFarmaco() {
            JDialog selezioneDialog = new JDialog(this, "Seleziona Farmaco", true);
            selezioneDialog.setLayout(new BorderLayout());

            // Pannello per selezione farmaco esistente
            JPanel selezionePanel = new JPanel(new BorderLayout());
            List<Farmaco> tuttiFarmaci = farmacoController.getFarmaci();
            DefaultComboBoxModel<Farmaco> farmaciModel = new DefaultComboBoxModel<>();
            tuttiFarmaci.forEach(farmaciModel::addElement);

            JComboBox<Farmaco> farmaciCombo = new JComboBox<>(farmaciModel);
            JButton selezionaEsistenteButton = utilities.createButton("Seleziona", utilities.Blue);

            selezionePanel.add(new JLabel("Farmaci esistenti:"), BorderLayout.NORTH);
            selezionePanel.add(farmaciCombo, BorderLayout.CENTER);
            selezionePanel.add(selezionaEsistenteButton, BorderLayout.SOUTH);

            // Pannello per aggiungere nuovo farmaco
            JPanel nuovoFarmacoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            nuovoFarmacoPanel.setBorder(BorderFactory.createTitledBorder("Aggiungi nuovo farmaco"));

            JTextField nomeField = new JTextField();
            JTextField produttoreField = new JTextField();
            JButton aggiungiNuovoButton = utilities.createButton("Aggiungi nuovo", utilities.Green);

            nuovoFarmacoPanel.add(new JLabel("Nome:"));
            nuovoFarmacoPanel.add(nomeField);
            nuovoFarmacoPanel.add(new JLabel("Produttore:"));
            nuovoFarmacoPanel.add(produttoreField);
            nuovoFarmacoPanel.add(new JLabel());
            nuovoFarmacoPanel.add(aggiungiNuovoButton);

            // Aggiungi listener ai pulsanti
            selezionaEsistenteButton.addActionListener(e -> {
                Farmaco selected = (Farmaco)farmaciCombo.getSelectedItem();
                if (selected != null && !farmaciSelezionati.contains(selected.getId())) {
                    farmaciSelezionati.add(selected.getId());
                    farmaciSelezionatiModel.addElement(selected.getNome() + " (" + selected.getProduttore() + ")");
                    selezioneDialog.dispose();
                }
            });

            aggiungiNuovoButton.addActionListener(e -> {
                try {
                    String nome = nomeField.getText().trim();
                    String produttore = produttoreField.getText().trim();

                    if (nome.isEmpty() || produttore.isEmpty()) {
                        throw new IllegalArgumentException("Inserire nome e produttore");
                    }

                    int nuovoId = farmacoController.inserisciFarmaco(nome, produttore);
                    farmaciSelezionati.add(nuovoId);
                    farmaciSelezionatiModel.addElement(nome + " (" + produttore + ")");
                    selezioneDialog.dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(selezioneDialog,
                            "Errore: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // Layout del dialogo
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(selezionePanel);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(nuovoFarmacoPanel);

            selezioneDialog.add(mainPanel, BorderLayout.CENTER);
            selezioneDialog.pack();
            selezioneDialog.setLocationRelativeTo(this);
            selezioneDialog.setVisible(true);
        }
    }
}

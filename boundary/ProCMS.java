package boundary;

import javax.swing.*;
import java.awt.*;

// importiamo le exception necessarie per la gestione degli errori
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.List;
import java.util.Date;


// solo i controller vanno importanti nella GUI
import controller.AnimaleController;
import controller.PrenotazioneController;

// entity importare per crare delle List di lettura, seconod quanto detto da Marco Ariano andrebbero riportati dei DTO
import entity.Animale;
import entity.Prenotazione;


public class ProCMS {
    private JPanel proPanel;
    private JButton logoutButton;
    private JButton aggiungiAnimaleButton;
    private JButton effettuaPrenotazioneButton;
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
        azioniPanel.add(Box.createVerticalStrut(10));

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

        aggiungiAnimaleButton.addActionListener(e -> {
            try {
                new inserisciAnimale(frame, username).setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                utilities.showErrorMessage(proPanel,"Errore nel'inserimento dell'animale: " + ex.getMessage());
            }
        });

        effettuaPrenotazioneButton.addActionListener(e -> {
            try {
                new effettuaPrenotazione(frame, username).setVisible(true);
            } catch (Exception ex) {
                utilities.showErrorMessage(proPanel,"Errore durante la prenotazione: " + ex.getMessage());
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


// Dialog per effettuare una prenotazione con calendario visuale
private static class effettuaPrenotazione extends JDialog{
    private Date dataSelezionata = null;
    private JLabel dataSelezionataLabel;

    public effettuaPrenotazione(JFrame parente, String usernameProprietario) throws SQLException, ClassNotFoundException {
        super(parente, "Effettua Prenotazione", true);
        setSize(800, 700);
        setLocationRelativeTo(parente);
        setLayout(new BorderLayout());

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 250, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Calendario personalizzato
        JPanel calendarioPanel = createAdvancedCalendar();
        mainPanel.add(calendarioPanel, BorderLayout.CENTER);

        // Pannello inferiore per prenotazioni e bottoni
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label data selezionata
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        dataSelezionataLabel = new JLabel("Nessuna data selezionata", JLabel.CENTER);
        dataSelezionataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dataSelezionataLabel.setForeground(new Color(70, 130, 180));
        bottomPanel.add(dataSelezionataLabel, gbc);

        // ComboBox prenotazioni
        gbc.gridy = 1; gbc.gridwidth = 1;
        bottomPanel.add(new JLabel("Prenotazioni disponibili:"), gbc);

        gbc.gridx = 1;
        JComboBox<Prenotazione> prenotazioneCombo = new JComboBox<>();
        prenotazioneCombo.setPreferredSize(new Dimension(250, 30));
        bottomPanel.add(prenotazioneCombo, gbc);

        // Renderer per la combo
        prenotazioneCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Prenotazione) {
                    Prenotazione p = (Prenotazione) value;
                    setText("Orario: " + p.getOrario() + ":00");
                } else {
                    setText("Seleziona prima una data");
                }
                return this;
            }
        });

        gbc.gridx = 2;
        JComboBox<Animale> animaleCombo = new JComboBox<>();
        animaleCombo.setPreferredSize(new Dimension(250, 30));
        bottomPanel.add(animaleCombo, gbc);

        // Renderer per la combo
        animaleCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Animale) {
                    Animale a = (Animale) value;
                    setText("Chip: " + a.getChip() + " Nome: " + a.getNome());
                } else {
                    setText("Seleziona prima un orario");
                }
                return this;
            }
        });



        // Bottoni
        gbc.gridy = 2; gbc.gridx = 0;
        JButton cercaButton = utilities.createButton("Cerca Prenotazioni", new Color(70, 130, 180));
        bottomPanel.add(cercaButton, gbc);

        gbc.gridx = 1;
        JButton confermaButton = utilities.createButton("Conferma", utilities.Green);
        bottomPanel.add(confermaButton, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton annullaButton = utilities.createButton("Annulla", utilities.DarkGray);
        bottomPanel.add(annullaButton, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // Event handlers
        cercaButton.addActionListener(e -> {
            if (dataSelezionata == null) {
                JOptionPane.showMessageDialog(this, "Seleziona una data dal calendario.");
                return;
            }

            PrenotazioneController prenotazioneController = new PrenotazioneController();
            AnimaleController animaleController = new AnimaleController();

            try {
                List<Prenotazione> prenotazioni = prenotazioneController.readPrenotazione(dataSelezionata, 0);
                prenotazioneCombo.removeAllItems();

                if (prenotazioni == null || prenotazioni.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nessuna prenotazione disponibile per la data selezionata.");
                } else {
                    for (Prenotazione p : prenotazioni) {
                        prenotazioneCombo.addItem(p);
                    }
                    JOptionPane.showMessageDialog(this, "Trovate " + prenotazioni.size() + " prenotazioni disponibili.");
                }

                List<Animale> animali = animaleController.readAnimale(usernameProprietario);
                animaleCombo.removeAllItems();

                if (animali == null || animali.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nessun animale trovato per l'utente.");
                } else {
                    for (Animale a : animali) {
                        animaleCombo.addItem(a);
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore durante la ricerca: " + ex.getMessage());
            }
        });



        confermaButton.addActionListener(e -> {
            Prenotazione selected = (Prenotazione) prenotazioneCombo.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Seleziona una prenotazione.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            JOptionPane.showMessageDialog(this, "Prenotazione confermata per il " +
                    sdf.format(dataSelezionata) + " alle " + selected.getOrario() + ":00");
            dispose();
        });

        annullaButton.addActionListener(e -> dispose());
    }

    private JPanel createAdvancedCalendar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Seleziona Data"));
        panel.setBackground(new Color(245, 250, 255));

        // Header con mese e anno
        JPanel headerPanel = new JPanel(new FlowLayout());
        headerPanel.setBackground(new Color(245, 250, 255));

        Calendar cal = Calendar.getInstance();

        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        JLabel meseAnnoLabel = new JLabel();

        prevButton.setPreferredSize(new Dimension(60, 40));
        nextButton.setPreferredSize(new Dimension(60, 40));

        updateMeseAnnoLabel(meseAnnoLabel, cal);

        headerPanel.add(prevButton);
        headerPanel.add(meseAnnoLabel);
        headerPanel.add(nextButton);

        // Griglia giorni
        JPanel giorniPanel = new JPanel(new GridLayout(7, 7, 2, 2));
        giorniPanel.setBackground(new Color(245, 250, 255));

        // Header giorni settimana
        String[] giorni = {"Dom", "Lun", "Mar", "Mer", "Gio", "Ven", "Sab"};
        for (String giorno : giorni) {
            JLabel label = new JLabel(giorno, JLabel.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(new Color(70, 130, 180));
            giorniPanel.add(label);
        }

        // Bottoni per i giorni
        JButton[][] giorniButtoms = new JButton[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                giorniButtoms[i][j] = new JButton();
                giorniButtoms[i][j].setPreferredSize(new Dimension(40, 40));
                giorniButtoms[i][j].setBackground(Color.WHITE);
                giorniButtoms[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                final int row = i, col = j;
                giorniButtoms[i][j].addActionListener(e -> {
                    // Reset colori
                    for (int x = 0; x < 6; x++) {
                        for (int y = 0; y < 7; y++) {
                            giorniButtoms[x][y].setBackground(Color.WHITE);
                        }
                    }

                    // Evidenzia selezionato
                    giorniButtoms[row][col].setBackground(new Color(70, 130, 180));
                    giorniButtoms[row][col].setForeground(Color.WHITE);

                    // Calcola data selezionata
                    String giornoText = giorniButtoms[row][col].getText();
                    if (!giornoText.isEmpty()) {
                        try {
                            int giorno = Integer.parseInt(giornoText);
                            Calendar newCal = (Calendar) cal.clone();
                            newCal.set(Calendar.DAY_OF_MONTH, giorno);
                            dataSelezionata = newCal.getTime();

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            dataSelezionataLabel.setText("Data selezionata: " + sdf.format(dataSelezionata));
                        } catch (NumberFormatException ignored) {}
                    }
                });

                giorniPanel.add(giorniButtoms[i][j]);
            }
        }

        // Popola calendario
        Runnable updateCalendar = () -> {
            updateMeseAnnoLabel(meseAnnoLabel, cal);
            populateCalendar(giorniButtoms, cal);
        };

        prevButton.addActionListener(e -> {
            cal.add(Calendar.MONTH, -1);
            updateCalendar.run();
        });

        nextButton.addActionListener(e -> {
            cal.add(Calendar.MONTH, 1);
            updateCalendar.run();
        });

        updateCalendar.run();

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(giorniPanel, BorderLayout.CENTER);

        return panel;
    }

    private void updateMeseAnnoLabel(JLabel label, Calendar cal) {
        String[] mesi = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
        label.setText(mesi[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    private void populateCalendar(JButton[][] buttons, Calendar cal) {
        Calendar tempCal = (Calendar) cal.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);

        int startDay = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Pulisci tutti i bottoni
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setForeground(Color.BLACK);
                buttons[i][j].setEnabled(false);
            }
        }

        // Popola con i giorni del mese
        Calendar today = Calendar.getInstance();
        int currentDay = 1;

        for (int week = 0; week < 6 && currentDay <= daysInMonth; week++) {
            for (int day = 0; day < 7 && currentDay <= daysInMonth; day++) {
                if (week == 0 && day < startDay) {
                    continue;
                }

                buttons[week][day].setText(String.valueOf(currentDay));

                // Controlla se è oggi o nel futuro
                tempCal.set(Calendar.DAY_OF_MONTH, currentDay);
                if (!tempCal.before(today)) {
                    buttons[week][day].setEnabled(true);
                } else {
                    buttons[week][day].setForeground(Color.LIGHT_GRAY);
                }

                currentDay++;
            }
        }
    }
}
}
package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// importiamo le exception necessarie per la gestione degli errori
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import controller.UtenteController;
import entity.Animale;
import entity.Prenotazione;
import entity.Utente;


public class ProCMS {
    private JPanel proPanel;
    private JButton logoutButton;

    private JButton effettuaPrenotazioneButton;
    private JButton modificaProfiloButton;
    private JLabel welcomeLabel;
    private JButton visualizzaAnimaliButton;

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
        visualizzaAnimaliButton = utilities.createButton("Visualizza Animali", utilities.Blue);
        effettuaPrenotazioneButton = utilities.createButton("Effettua Prenotazione",utilities.Green);

        animaliPanel.add(effettuaPrenotazioneButton);
        animaliPanel.add(Box.createVerticalStrut(10));

        animaliPanel.add(visualizzaAnimaliButton);
        animaliPanel.add(Box.createVerticalStrut(10));

        proPanel.add(animaliPanel);
    }
    //visualizzazione
    private void createActionsSection() {
        JPanel azioniPanel = utilities.createSectionPanel("Altre Azioni");
        modificaProfiloButton = utilities.createButton("Modifica Profilo", utilities.Orange);
        logoutButton = utilities.createButton("Logout", utilities.Red);

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

        effettuaPrenotazioneButton.addActionListener(e -> {
            try {
                new effettuaPrenotazione(frame, username).setVisible(true);
            } catch (Exception ex) {
                utilities.showErrorMessage(proPanel,"Errore durante la prenotazione: " + ex.getMessage());
            }
        });

        modificaProfiloButton.addActionListener(e -> {
            try{
                new modificaProfilo(frame, username).setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                utilities.showErrorMessage(proPanel,"Errore durante la modifica del profilo: " + ex.getMessage());
            }
        });

        visualizzaAnimaliButton.addActionListener(e -> {
            try {
                new visualizzaAnimali(frame, username).setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                utilities.showErrorMessage(proPanel,"Errore durante la visualizzazione degli animali: " + ex.getMessage());
            }
        });
    }

    // Dialog per inserire un animale
    private static class inserisciAnimale extends JDialog {
        public inserisciAnimale(JFrame parente, String usernameProprietario) throws SQLException, ClassNotFoundException {
            super(parente, "Inserisci Animale", true);

            // Form principale come sezione coerente
            JPanel formPanel = utilities.createSectionPanel("Inserisci Animale");
            formPanel.setLayout(new GridLayout(0, 2, 10, 10));

            // Campi input
            formPanel.add(new JLabel("Chip:"));
            JTextField chipField = new JTextField();
            formPanel.add(chipField);

            formPanel.add(new JLabel("Nome:"));
            JTextField nomeField = new JTextField();
            formPanel.add(nomeField);

            formPanel.add(new JLabel("Tipo:"));
            JComboBox<String> tipoCombo = new JComboBox<>(utilities.animalTypes);
            tipoCombo.setSelectedIndex(0);
            formPanel.add(tipoCombo);

            formPanel.add(new JLabel("Razza:"));
            JTextField razzaField = new JTextField();
            formPanel.add(razzaField);

            formPanel.add(new JLabel("Colore:"));
            JTextField coloreField = new JTextField();
            formPanel.add(coloreField);

            formPanel.add(new JLabel("Data di Nascita (yyyy/MM/gg):"));
            JTextField dataNascitaField = new JTextField();
            formPanel.add(dataNascitaField);

            // Pulsanti separati in pannello dedicato
            JButton salvaButton = utilities.createButton("Salva", utilities.Blue);
            JButton annullaButton = utilities.createButton("Annulla", utilities.Red);
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(annullaButton);
            buttonPanel.add(salvaButton);

            // Aggiunta ai bordi del dialog
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(formPanel, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            // Eventi
            annullaButton.addActionListener(e -> dispose());

            salvaButton.addActionListener(e -> {
                // Estrai e valida i dati
                String chipText = chipField.getText().trim();
                String nome = nomeField.getText().trim();
                Object selectedItem = tipoCombo.getSelectedItem();
                String tipo = (selectedItem != null) ? selectedItem.toString() : "";
                String razza = razzaField.getText().trim();
                String colore = coloreField.getText().trim();
                String dataNascitaText = dataNascitaField.getText().trim();

                // Validazione campi
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
                try {
                    AnimaleController animaleController = new AnimaleController();
                    animaleController.addAnimale(chip, nome, tipo, razza, colore, dataNascita, usernameProprietario);
                    JOptionPane.showMessageDialog(this, "Animale inserito con successo.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + ex.getMessage());
                }
            });
            pack();
            setLocationRelativeTo(parente); // centra la finestra
        }
    }

    // Dialog per effettuare una prenotazione con calendario visuale
    private static class effettuaPrenotazione extends JDialog{
        private Date dataSelezionata = null;
        private JLabel dataSelezionataLabel;

        public effettuaPrenotazione(JFrame parente, String usernameProprietario) throws SQLException, ClassNotFoundException {
            super(parente, "Effettua Prenotazione", true);

            PrenotazioneController prenotazioneController = new PrenotazioneController();
            AnimaleController animaleController = new AnimaleController();

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
                        setText("Seleziona prima una data");
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
                Prenotazione dataSelected = (Prenotazione) prenotazioneCombo.getSelectedItem();
                Animale animaleSelected = (Animale) animaleCombo.getSelectedItem();
                int orarioSelezionato = dataSelected != null ? dataSelected.getOrario() : -1;

                if (dataSelected == null) {
                    JOptionPane.showMessageDialog(this, "Seleziona una data.");
                    return;
                }
                if (animaleSelected == null) {
                    JOptionPane.showMessageDialog(this, "Seleziona un animale.");
                    return;
                }
                if (orarioSelezionato == -1) {
                    JOptionPane.showMessageDialog(this, "Seleziona un orario valido.");
                    return;
                }

                try {
                    prenotazioneController.updatePrenotazione(dataSelezionata, orarioSelezionato, animaleSelected.getChip(), 1);
                    JOptionPane.showMessageDialog(this, "Prenotazione effettuata con successo.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante la prenotazione: " + ex.getMessage());
                }
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

    private static class modificaProfilo extends JDialog {
        public modificaProfilo(JFrame parente, String usernameUtente) throws SQLException, ClassNotFoundException {
            super(parente, "Modifica profilo", true);

            UtenteController utenteController = new UtenteController();
            List<Utente> utenti = utenteController.getUser(usernameUtente);

            if (utenti == null || utenti.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun utente trovato con username: " + usernameUtente);
                return;
            }

            Utente utente = utenti.get(0);

            // Form - simile a N1
            JPanel formPanel = utilities.createSectionPanel("Modifica Profilo");
            formPanel.setLayout(new GridLayout(0, 2, 10, 10));

            formPanel.add(new JLabel("Nome:"));
            JTextField nomeField = new JTextField(utente.getNome());
            formPanel.add(nomeField);

            formPanel.add(new JLabel("Cognome:"));
            JTextField cognomeField = new JTextField(utente.getCognome());
            formPanel.add(cognomeField);

            formPanel.add(new JLabel("Email:"));
            JTextField emailField = new JTextField(utente.getEmail());
            formPanel.add(emailField);

            formPanel.add(new JLabel("Username:"));
            JTextField usernameField = new JTextField(utente.getUsername());
            formPanel.add(usernameField);

            formPanel.add(new JLabel("Password:"));
            JPasswordField passwordField = new JPasswordField(utente.getPassword());
            formPanel.add(passwordField);

            // Pannello pulsanti separato
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton modificaButton = utilities.createButton("Modifica", utilities.Orange);
            JButton annullaButton = utilities.createButton("Annulla", utilities.Red);
            buttonPanel.add(modificaButton);
            buttonPanel.add(annullaButton);

            // Aggiunta dei pannelli al JDialog
            getContentPane().add(formPanel, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            // Eventi
            annullaButton.addActionListener(e -> dispose());

            modificaButton.addActionListener(e -> {
                try {
                    utenteController.updateUser(
                            usernameField.getText(),
                            nomeField.getText(),
                            cognomeField.getText(),
                            emailField.getText(),
                            new String(passwordField.getPassword()),
                            utente.getImmagineProfilo(),
                            usernameUtente
                    );

                    JOptionPane.showMessageDialog(this, "Profilo aggiornato con successo.");

                    // Ricarico la schermata principale con il nuovo username
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    frame.setContentPane(new ProCMS(frame, nomeField.getText(), cognomeField.getText(), usernameField.getText()).getProPanel());
                    frame.revalidate();
                    frame.repaint();
                    dispose();

                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Errore durante l'aggiornamento del profilo.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            });

            pack();
            setLocationRelativeTo(parente); // centra la finestra rispetto al padre
        }
    }

    private static class visualizzaAnimali extends JDialog {
        private DefaultTableModel model;
        private JTable table;
        private List<Animale> animali;
        private AnimaleController animaleController;
        private String usernameProprietario;
        private JFrame parente;

        public visualizzaAnimali(JFrame parente, String usernameProprietario) throws SQLException, ClassNotFoundException {
            super(parente, "Visualizza Animali", true);
            this.parente = parente;
            this.usernameProprietario = usernameProprietario;
            this.animaleController = new AnimaleController();

            setSize(800, 600);
            setLocationRelativeTo(parente);

            // Pannello principale
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(245, 250, 255));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Pulsante Inserisci Animale
            JButton inserisciAnimaleButton = utilities.createButton("Inserisci Animale", utilities.Blue);
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            headerPanel.setBackground(new Color(245, 250, 255));
            headerPanel.add(inserisciAnimaleButton);
            mainPanel.add(headerPanel, BorderLayout.NORTH);

            // Inizializza la lista animali e il modello
            refreshAnimaliList();

            // Colonne della tabella
            String[] columnNames = {"Chip", "Nome", "Razza", "Tipo", "Data di nascita" ," ", "  "};

            // Modello tabella
            model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4 || column == 5;
                }
            };

            // Riempimento iniziale del modello
            updateTableModel();

            // Tabella
            table = new JTable(model);
            table.setRowHeight(30);

            // Configura renderer e editor per i pulsanti
            setupButtonColumns();

            // Listener per il pulsante Inserisci Animale
            inserisciAnimaleButton.addActionListener(e -> {
                inserisciAnimale dialog = null;
                try {
                    dialog = new inserisciAnimale(parente, usernameProprietario);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        refreshTableData();
                    }
                });
                dialog.setVisible(true);
            });

            // Scroll pane
            JScrollPane scrollPane = new JScrollPane(table);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            add(mainPanel, BorderLayout.CENTER);
        }

        private void refreshAnimaliList() throws SQLException, ClassNotFoundException {
            animali = animaleController.readAnimale(usernameProprietario);
        }

        private void updateTableModel() {
            model.setRowCount(0); // Pulisci la tabella
            for (Animale animale : animali) {
                model.addRow(new Object[]{
                        animale.getChip(),
                        animale.getNome(),
                        animale.getRazza(),
                        animale.getTipo(),
                        animale.getDataNascita(),
                        "Modifica",
                        "Cancella"
                });
            }
        }

        private void refreshTableData() {
            try {
                refreshAnimaliList();
                updateTableModel();
                table.revalidate();
                table.repaint();
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(parente,
                        "Errore durante il caricamento degli animali: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);}
        }

        private void setupButtonColumns() {
            // Renderer per i pulsanti
            table.getColumn(" ").setCellRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                    JButton btnModifica = new JButton("Modifica");
                    btnModifica.setOpaque(true);
                    btnModifica.setFocusable(false);
                    panel.add(btnModifica);
                    return panel;
                }
            });

            table.getColumn("  ").setCellRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                    JButton btnCancella = new JButton("Cancella");
                    btnCancella.setOpaque(true);
                    btnCancella.setFocusable(false);
                    panel.add(btnCancella);
                    return panel;
                }
            });

            // Editor per Modifica
            table.getColumn(" ").setCellEditor(new DefaultCellEditor(new JTextField()) {
                private JPanel panel;
                private JButton btnModifica = new JButton("Modifica");

                {
                    panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                    panel.add(btnModifica);

                    btnModifica.addActionListener(e -> {
                        int row = table.getEditingRow();
                        Animale animale = animali.get(row);

                        JPanel formPanelAnimal = utilities.createSectionPanel("Modifica Animale");

                        formPanelAnimal.setLayout(new GridLayout(0, 2, 10, 10));

                        formPanelAnimal.add(new JLabel("Chip:"));
                        JTextField chipField = new JTextField(String.valueOf(animale.getChip()));
                        formPanelAnimal.add(chipField);

                        formPanelAnimal.add(new JLabel("Nome:"));
                        JTextField nomeField = new JTextField(animale.getNome());
                        formPanelAnimal.add(nomeField);

                        formPanelAnimal.add(new JLabel("Tipo:"));
                        JComboBox<String> tipoCombo = new JComboBox<>(utilities.animalTypes);

                        tipoCombo.setSelectedItem(animale.getTipo());
                        formPanelAnimal.add(tipoCombo);

                        formPanelAnimal.add(new JLabel("Razza:"));
                        JTextField razzaField = new JTextField(animale.getRazza());
                        formPanelAnimal.add(razzaField);

                        formPanelAnimal.add(new JLabel("Colore:"));
                        JTextField coloreField = new JTextField(animale.getColore());
                        formPanelAnimal.add(coloreField);

                        formPanelAnimal.add(new JLabel("Data di Nascita (yyyy/MM/gg):"));
                        JTextField dataNascitaField = new JTextField(new SimpleDateFormat("yyyy/MM/dd").format(animale.getDataNascita()));
                        formPanelAnimal.add(dataNascitaField);

                        int conferma = JOptionPane.showConfirmDialog(parente, formPanelAnimal,
                                "Modifica Animale", JOptionPane.OK_CANCEL_OPTION);
                        if (conferma == JOptionPane.OK_OPTION) {
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
                                JOptionPane.showMessageDialog(parente, "Tutti i campi sono obbligatori.");
                                return;
                            }

                            int chip;
                            try {
                                chip = Integer.parseInt(chipText);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(parente, "Il chip deve essere un numero intero.");
                                return;
                            }

                            Date dataNascita;
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                sdf.setLenient(false);
                                dataNascita = sdf.parse(dataNascitaText);
                            } catch (ParseException ex) {
                                JOptionPane.showMessageDialog(parente, "Formato data non valido. Usa aaaa/MM/gg.");
                                return;
                            }

                            // Salvataggio
                            try {
                                animaleController.updateAnimale(chip, nome, tipo, razza, colore, dataNascita, animale.getChip());
                                JOptionPane.showMessageDialog(parente, "Animale modificato con successo.");
                                refreshTableData();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(parente, "Errore durante la modifica: " + ex.getMessage());
                            }
                        }
                    });
                }

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    return panel;
                }

                @Override
                public Object getCellEditorValue() {
                    return null;
                }
            });

            // Editor per Cancella
            table.getColumn("  ").setCellEditor(new DefaultCellEditor(new JTextField()) {
                private JPanel panel;
                private JButton btnCancella = new JButton("Cancella");

                {
                    panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                    panel.add(btnCancella);

                    btnCancella.addActionListener(e -> {
                        int row = table.getEditingRow();
                        Animale animale = animali.get(row);

                        int conferma = JOptionPane.showConfirmDialog(parente,
                                "Sei sicuro di voler cancellare l'animale \"" + animale.getNome() + "\"?",
                                "Conferma Cancellazione", JOptionPane.YES_NO_OPTION);

                        if (conferma == JOptionPane.YES_OPTION) {
                            try {
                                animaleController.deleteAnimale(animale.getChip());
                                model.removeRow(row);
                                animali.remove(row);
                                JOptionPane.showMessageDialog(parente,
                                        "Animale \"" + animale.getNome() + "\" cancellato con successo.");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(parente,
                                        "Errore durante la cancellazione: " + ex.getMessage(),
                                        "Errore", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        fireEditingStopped();
                    });
                }

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    return panel;
                }

                @Override
                public Object getCellEditorValue() {
                    return null;
                }
            });
        }
    }

}

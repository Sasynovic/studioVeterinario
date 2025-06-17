package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// importiamo le exception necessarie per la gestione degli errori
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Date;

// solo i controller vanno importanti nella GUI
import controller.AnimaleController;
import controller.UtenteController;
import controller.AgendaController;
import controller.PrenotazioneController;

// entity importare per crare delle List di lettura, seconod quanto detto da Marco Ariano andrebbero riportati dei DTO
import entity.Agenda;
import entity.Animale;
import entity.Utente;

public class ProCMS {
    private JPanel proPanel;
    private JButton logoutButton;

    private JButton effettuaPrenotazioneButton;
    private JButton modificaProfiloButton;
    private JLabel welcomeLabel;
    private JButton visualizzaAnimaliButton;

    private static Utilities utilities = new Utilities();

    public ProCMS(JFrame frame, String nome, String cognome, String username, String immagineProfilo) {
        initializeMainPanel();
        createWelcomeLabel(nome, cognome, immagineProfilo);
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
    private void createWelcomeLabel(String nome, String cognome, String immagineProfilo) {
        welcomeLabel = new JLabel("Benvenuto " + nome + " " + cognome);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        ImageIcon logo = new ImageIcon(getClass().getResource("/images/propic/" + immagineProfilo));
        JLabel imageLabel = new JLabel(logo);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setImage(logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        proPanel.add(imageLabel);
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
    private static class effettuaPrenotazione extends JDialog {
        private Date dataSelezionata = null;
        private JLabel dataSelezionataLabel;

        public effettuaPrenotazione(JFrame parente, String usernameProprietario) throws SQLException, ClassNotFoundException {
            super(parente, "Effettua Prenotazione", true);

            AgendaController ac = new AgendaController();
            AnimaleController animaleController = new AnimaleController();

            setSize(800, 700);
            setLocationRelativeTo(parente);
            setLayout(new BorderLayout());

            // Pannello principale
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(245, 250, 255));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Calendario personalizzato
            dataSelezionataLabel = new JLabel("Nessuna data selezionata", JLabel.CENTER);
            dataSelezionataLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            dataSelezionataLabel.setForeground(new Color(70, 130, 180));

            // Creazione del calendario con callback per la selezione della data
            JPanel calendarioPanel = utilities.createAdvancedCalendar(dataSelezionataLabel, selectedDate -> {
                dataSelezionata = selectedDate;
            });
            mainPanel.add(calendarioPanel, BorderLayout.CENTER);

            // Pannello inferiore per prenotazioni e bottoni
            JPanel bottomPanel = new JPanel(new GridBagLayout());
            bottomPanel.setBackground(new Color(245, 250, 255));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            // Label data selezionata
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            bottomPanel.add(dataSelezionataLabel, gbc);

            // ComboBox prenotazioni
            gbc.gridy = 1; gbc.gridwidth = 1;
            bottomPanel.add(new JLabel("Prenotazioni disponibili:"), gbc);

            gbc.gridx = 1;
            JComboBox<Agenda> prenotazioneCombo = new JComboBox<>();
            prenotazioneCombo.setPreferredSize(new Dimension(250, 30));
            bottomPanel.add(prenotazioneCombo, gbc);

            // Renderer per la combo
            prenotazioneCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                                                              int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Agenda) {
                        Agenda a = (Agenda) value;
                        setText("Orario: " + a.getOrario() + ":00");
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
            JButton cercaButton = utilities.createButton("Cerca Prenotazioni", utilities.Blue);
            bottomPanel.add(cercaButton, gbc);

            gbc.gridx = 1;
            JButton confermaButton = utilities.createButton("Conferma", utilities.Green);
            bottomPanel.add(confermaButton, gbc);

            gbc.gridx = 2;
            JButton annullaButton = utilities.createButton("Annulla", utilities.Red);
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
                    List<Agenda> agenda = ac.getPrenotazioniDisponibili(dataSelezionata);
                    prenotazioneCombo.removeAllItems();

                    if (agenda == null || agenda.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nessuna prenotazione disponibile per la data selezionata.");
                        animaleCombo.removeAllItems();
                    } else {
                        for (Agenda a : agenda) {
                            prenotazioneCombo.addItem(a);
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
                        JOptionPane.showMessageDialog(this, "Trovate " + agenda.size() + " prenotazioni disponibili.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante la ricerca: " + ex.getMessage());
                }
            });

            confermaButton.addActionListener(e -> {
                Agenda dataSelected = (Agenda) prenotazioneCombo.getSelectedItem();
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
                    PrenotazioneController prenotazioneController = new PrenotazioneController();
                    prenotazioneController.updatePrenotazioneUtente(dataSelezionata, orarioSelezionato, animaleSelected.getChip(), 1);
                    JOptionPane.showMessageDialog(this, "Prenotazione effettuata con successo.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Errore durante la prenotazione: " + ex.getMessage());
                }
            });

            annullaButton.addActionListener(e -> dispose());
        }
    }
    private class modificaProfilo extends JDialog {
        private File selectedImageFile;
        private JLabel previewLabel;
        private static final String DEFAULT_PROFILE_IMAGE = "default.png";
        private static final int PREVIEW_SIZE = 100;

        public modificaProfilo(JFrame parente, String usernameUtente) throws SQLException, ClassNotFoundException {
            super(parente, "Modifica profilo", true);

            UtenteController utenteController = new UtenteController();
            List<Utente> utenti = utenteController.getUser(usernameUtente);

            if (utenti == null || utenti.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun utente trovato con username: " + usernameUtente);
                return;
            }

            Utente utente = utenti.get(0);

            // Form principale con larghezza fissa
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Margini interni
            mainPanel.setPreferredSize(new Dimension(650, 450)); // Larghezza fissa 450px, altezza variabile

            JPanel formPanel = utilities.createSectionPanel("Modifica Profilo");
            formPanel.setLayout(new GridLayout(0, 2, 10, 10));

            // Campi testo
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

            // Sezione immagine profilo
            formPanel.add(new JLabel("Immagine Profilo:"));
            JPanel imagePanel = createImagePanel("images/propic/" + utente.getImmagineProfilo());
            formPanel.add(imagePanel);

            // Pannello pulsanti
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton modificaButton = utilities.createButton("Modifica", utilities.Orange);
            JButton annullaButton = utilities.createButton("Annulla", utilities.Red);
            buttonPanel.add(modificaButton);
            buttonPanel.add(annullaButton);

            // Aggiunta dei componenti al pannello principale
            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Configurazione finale della finestra
            getContentPane().add(mainPanel);
            pack();
            setResizable(false); // Impedisce il ridimensionamento
            setLocationRelativeTo(parente);
            // Eventi
            annullaButton.addActionListener(e -> dispose());

            modificaButton.addActionListener(e -> {
                try {
                    String imagePath;
                    if (selectedImageFile != null) {
                        String savedPath = saveProfileImage(selectedImageFile);
                        File savedFile = new File(savedPath);
                        waitForImageWrite(savedFile, 10, 200); // aspetta max 1 secondo
                        imagePath = savedPath;
                    } else {
                        imagePath = utente.getImmagineProfilo();
                    }

                    utenteController.updateUser(
                            usernameField.getText(),
                            nomeField.getText(),
                            cognomeField.getText(),
                            emailField.getText(),
                            new String(passwordField.getPassword()),
                            imagePath,
                            usernameUtente
                    );
                    JOptionPane.showMessageDialog(this, "Profilo aggiornato con successo.");

                    // Chiudi questa finestra di dialogo
                    dispose();

                    JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(ProCMS.this.proPanel);

                    // Ricarica la schermata principale
                    mainFrame.dispose(); // chiudi la finestra corrente

// Ricrea la finestra principale
                    SwingUtilities.invokeLater(() -> {
                        try {
                            JFrame newFrame = new JFrame("ProCMS");
                            System.out.println("Nuova immagine inserita: " + imagePath);
                            ProCMS nuovoProCMS = new ProCMS(
                                    newFrame,
                                    nomeField.getText(),
                                    cognomeField.getText(),
                                    usernameField.getText(),
                                    imagePath
                            );
                            newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            newFrame.setContentPane(nuovoProCMS.getProPanel());
                            newFrame.pack();
                            newFrame.setLocationRelativeTo(null);
                            newFrame.setVisible(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Errore durante il riavvio: " + ex.getMessage());
                        }
                    });


                } catch (SQLException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Errore durante l'aggiornamento del profilo: " + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            pack();
            setLocationRelativeTo(parente);
        }

        private JPanel createImagePanel(String currentImagePath) {
            JPanel panel = new JPanel(new BorderLayout());

            // Anteprima immagine
            previewLabel = new JLabel();
            previewLabel.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
            previewLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            loadImage(currentImagePath);

            // Bottone selezione
            JButton selectButton = new JButton("Cambia Immagine");
            selectButton.addActionListener(e -> selectProfileImage());

            panel.add(previewLabel, BorderLayout.CENTER);
            panel.add(selectButton, BorderLayout.SOUTH);

            return panel;
        }

        private void loadImage(String imagePath) {
            ImageIcon icon = null;

            try {
                if (imagePath != null && !imagePath.equals(DEFAULT_PROFILE_IMAGE)) {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        icon = new ImageIcon(imageFile.getAbsolutePath());
                    } else {
                        System.err.println("Immagine non trovata su disco: " + imagePath);
                    }
                }

                if (icon == null) {
                    URL resource = getClass().getResource("/images/propic/" + DEFAULT_PROFILE_IMAGE);
                    if (resource != null) {
                        icon = new ImageIcon(resource);
                    } else {
                        System.err.println("Immagine default non trovata nel classpath!");
                    }
                }

                if (icon != null && icon.getImage() != null) {
                    Image img = icon.getImage().getScaledInstance(
                            PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_SMOOTH);
                    previewLabel.setIcon(new ImageIcon(img));
                } else {
                    previewLabel.setIcon(null);
                    previewLabel.setText("Immagine non disponibile");
                }

            } catch (Exception e) {
                e.printStackTrace();
                previewLabel.setIcon(null);
                previewLabel.setText("Errore caricamento");
            }
        }

        private void selectProfileImage() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Immagini", "jpg", "jpeg", "png", "gif"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedImageFile = fileChooser.getSelectedFile();
                updateImagePreview(selectedImageFile);
            }
        }

        private void updateImagePreview(File imageFile) {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(
                    PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_SMOOTH);
            previewLabel.setIcon(new ImageIcon(img));
        }

        private String saveProfileImage(File imageFile) {
            File profileDir = new File("images/propic/");
            if (!profileDir.exists()) {
                profileDir.mkdirs();
            }

            String timestamp = String.valueOf(System.currentTimeMillis());
            String extension = imageFile.getName().substring(imageFile.getName().lastIndexOf("."));
            String newFileName = "img_" + timestamp + extension;

            try {
                File destination = new File(profileDir, newFileName);
                System.out.println("Copying image to: " + destination.getAbsolutePath());
                java.nio.file.Files.copy(
                        imageFile.toPath(),
                        destination.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                return newFileName;
            } catch (Exception e) {
                e.printStackTrace();
                return DEFAULT_PROFILE_IMAGE;
            }
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
                    JButton btnModifica = utilities.createButton("Modifica", utilities.Orange);
                    panel.add(btnModifica);
                    return panel;
                }
            });

            table.getColumn("  ").setCellRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                    JButton btnCancella = utilities.createButton("Cancella", utilities.Red);
                    panel.add(btnCancella);

                    btnCancella.addActionListener(e -> {
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
                    });
                    return panel;
                }
            });

            // Editor per Modifica
            table.getColumn(" ").setCellEditor(new DefaultCellEditor(new JTextField()) {
                private JPanel panel;
                private JButton btnModifica = utilities.createButton("Modifica", utilities.Orange);

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
    private void waitForImageWrite(File file, int maxAttempts, int delayMillis) {
        int attempts = 0;
        while (!file.exists() && attempts < maxAttempts) {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            attempts++;
        }
    }

}

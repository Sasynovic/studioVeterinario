package boundary;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import database.UtenteDAO;
import database.AnimaleDAO;
import entity.Proprietario;
import entity.Animale;

public class ProCMS {
    private JPanel proPanel;
    private JButton logoutButton;
    private JButton effettuaPrenotazioneButton;
    private JButton elencoAnimaliButton;
    private JButton cancellaAnimaliButton;
    private JButton visualizzaPrenotazioniEffettuateButton;
    private JButton modificaProfiloButton;
    private JLabel welcomeLabel;

    public ProCMS(JFrame frame, String nome, String cognome, String username) {
        initializeMainPanel();
        createWelcomeLabel(nome, cognome);
        createAnimalsSection();
        createActionsSection();
        setupEventHandlers(frame, username);
    }

    private void initializeMainPanel() {
        proPanel = new JPanel();
        proPanel.setBackground(new Color(245, 250, 255));
        proPanel.setLayout(new BoxLayout(proPanel, BoxLayout.Y_AXIS));
        proPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
    }

    private void createWelcomeLabel(String nome, String cognome) {
        welcomeLabel = new JLabel("Benvenuto " + nome + " " + cognome);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        proPanel.add(welcomeLabel);
    }

    private void createAnimalsSection() {
        JPanel animaliPanel = createSectionPanel("Gestione Animali");
        elencoAnimaliButton = createButton("Elenco Animali", new Color(70, 130, 180));
        cancellaAnimaliButton = createButton("Cancella Animali", new Color(220, 20, 60));
        effettuaPrenotazioneButton = createButton("Effettua Prenotazione", new Color(186, 85, 211));
        visualizzaPrenotazioniEffettuateButton = createButton("Prenotazioni Effettuate", new Color(60, 179, 113));

        animaliPanel.add(elencoAnimaliButton);
        animaliPanel.add(Box.createVerticalStrut(10));
        animaliPanel.add(cancellaAnimaliButton);
        animaliPanel.add(Box.createVerticalStrut(10));
        animaliPanel.add(effettuaPrenotazioneButton);
        animaliPanel.add(Box.createVerticalStrut(10));
        animaliPanel.add(visualizzaPrenotazioniEffettuateButton);

        proPanel.add(animaliPanel);
    }

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

        elencoAnimaliButton.addActionListener(e -> {
            try {
                new VisualizzaAnimaliDialog(frame, username).setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                showErrorMessage("Errore nel caricamento degli animali: " + ex.getMessage());
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

    public JPanel getProPanel() {
        return proPanel;
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

    // Classe migliorata per la visualizzazione degli animali
    private static class VisualizzaAnimaliDialog extends JDialog {
        private static final int ROWS_PER_PAGE = 10;
        private static final String[] COLUMN_NAMES = {"#", "Chip", "Nome", "Razza", "Colore", "Data Nascita"};

        private final String username;
        private List<Animale> allAnimals; // Tutti gli animali originali
        private List<Animale> filteredAnimals; // Animali filtrati dalla ricerca

        private JTable table;
        private DefaultTableModel tableModel;
        private JTextField searchField;
        private JLabel statusLabel;
        private JButton prevButton;
        private JButton nextButton;
        private int currentPage = 0;

        public VisualizzaAnimaliDialog(JFrame parent, String username) throws SQLException, ClassNotFoundException {
            super(parent, "Animali di " + username, true);
            this.username = username;

            setSize(950, 700);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));
            ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            loadAnimalsData();
            if (allAnimals.isEmpty()) {
                showNoAnimalsMessage();
                return;
            }

            initializeComponents();
            setupLayout();
            refreshTable();
        }

        private void loadAnimalsData() throws SQLException, ClassNotFoundException {
            AnimaleDAO animaleDAO = new AnimaleDAO();
            allAnimals = animaleDAO.visualizzaAnimali(username);
            filteredAnimals = new ArrayList<>(allAnimals);
        }

        private void showNoAnimalsMessage() {
            JOptionPane.showMessageDialog(this,
                    "Nessun animale trovato per l'utente: " + username,
                    "Nessun Animale",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }

        private void initializeComponents() {
            // Campo di ricerca
            searchField = new JTextField(25);
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            searchField.setToolTipText("Cerca per nome, razza o colore");

            // Listener per ricerca in tempo reale
            searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
            });

            // Tabella
            tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Rende la tabella non editabile
                }
            };

            table = new JTable(tableModel);
            customizeTable();

            // Controlli di paginazione
            prevButton = new JButton("← Precedente");
            nextButton = new JButton("Successiva →");
            statusLabel = new JLabel();

            setupPaginationButtons();
        }

        private void customizeTable() {
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setRowHeight(28);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setBackground(new Color(70, 130, 180));
            table.getTableHeader().setForeground(Color.WHITE);
            table.setFillsViewportHeight(true);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setGridColor(new Color(220, 220, 220));
            table.setSelectionBackground(new Color(184, 207, 229));

            // Renderer per centrare il testo e alternare i colori delle righe
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    }
                    setHorizontalAlignment(JLabel.CENTER);
                    return c;
                }
            };

            // Applica il renderer a tutte le colonne
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Double-click per vedere i dettagli
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                        showAnimalDetails();
                    }
                }
            });
        }

        private void setupPaginationButtons() {
            prevButton.addActionListener(e -> {
                if (currentPage > 0) {
                    currentPage--;
                    refreshTable();
                }
            });

            nextButton.addActionListener(e -> {
                if (currentPage < getTotalPages() - 1) {
                    currentPage++;
                    refreshTable();
                }
            });
        }

        private void setupLayout() {
            // Pannello superiore con ricerca
            JPanel topPanel = new JPanel(new BorderLayout());
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchPanel.add(new JLabel("Cerca: "));
            searchPanel.add(searchField);

            JButton clearButton = new JButton("Pulisci");
            clearButton.addActionListener(e -> {
                searchField.setText("");
                performSearch();
            });
            searchPanel.add(clearButton);

            topPanel.add(searchPanel, BorderLayout.WEST);
            add(topPanel, BorderLayout.NORTH);

            // Tabella al centro
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Elenco Animali"));
            add(scrollPane, BorderLayout.CENTER);

            // Pannello inferiore con paginazione ed export
            JPanel bottomPanel = createBottomPanel();
            add(bottomPanel, BorderLayout.SOUTH);
        }

        private JPanel createBottomPanel() {
            JPanel bottomPanel = new JPanel(new BorderLayout());

            // Paginazione
            JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            paginationPanel.add(prevButton);
            paginationPanel.add(Box.createHorizontalStrut(20));
            paginationPanel.add(statusLabel);
            paginationPanel.add(Box.createHorizontalStrut(20));
            paginationPanel.add(nextButton);

            // Pulsante export
            JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton exportButton = new JButton("Esporta CSV");
            exportButton.addActionListener(e -> exportToCSV());
            exportPanel.add(exportButton);

            bottomPanel.add(paginationPanel, BorderLayout.CENTER);
            bottomPanel.add(exportPanel, BorderLayout.EAST);

            return bottomPanel;
        }

        private void performSearch() {
            String query = searchField.getText().trim().toLowerCase();

            if (query.isEmpty()) {
                filteredAnimals = new ArrayList<>(allAnimals);
            } else {
                filteredAnimals = allAnimals.stream()
                        .filter(animal ->
                                animal.getNome().toLowerCase().contains(query) ||
                                        animal.getRazza().toLowerCase().contains(query) ||
                                        animal.getColore().toLowerCase().contains(query) ||
                                        String.valueOf(animal.getChip()).contains(query)
                        )
                        .collect(java.util.stream.Collectors.toList());
            }

            currentPage = 0; // Reset alla prima pagina dopo la ricerca
            refreshTable();
        }

        private void refreshTable() {
            // Pulisce la tabella
            tableModel.setRowCount(0);

            // Aggiunge i dati della pagina corrente
            int start = currentPage * ROWS_PER_PAGE;
            int end = Math.min(start + ROWS_PER_PAGE, filteredAnimals.size());

            for (int i = start; i < end; i++) {
                Animale animal = filteredAnimals.get(i);
                Object[] rowData = {
                        i + 1, // Numero progressivo globale
                        animal.getChip(),
                        animal.getNome(),
                        animal.getRazza(),
                        animal.getColore(),
                        formatDate(animal.getDataNascita())
                };
                tableModel.addRow(rowData);
            }

            updatePaginationControls();
        }

        private void updatePaginationControls() {
            int totalPages = getTotalPages();

            prevButton.setEnabled(currentPage > 0);
            nextButton.setEnabled(currentPage < totalPages - 1);

            if (filteredAnimals.isEmpty()) {
                statusLabel.setText("Nessun risultato trovato");
            } else {
                int start = currentPage * ROWS_PER_PAGE + 1;
                int end = Math.min((currentPage + 1) * ROWS_PER_PAGE, filteredAnimals.size());
                statusLabel.setText(String.format("Pagina %d di %d - Mostrando %d-%d di %d animali",
                        currentPage + 1, Math.max(1, totalPages), start, end, filteredAnimals.size()));
            }
        }

        private int getTotalPages() {
            return filteredAnimals.isEmpty() ? 1 : (int) Math.ceil((double) filteredAnimals.size() / ROWS_PER_PAGE);
        }

        private void showAnimalDetails() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) return;

            int globalIndex = currentPage * ROWS_PER_PAGE + selectedRow;
            if (globalIndex >= filteredAnimals.size()) return;

            Animale animal = filteredAnimals.get(globalIndex);

            String details = String.format(
                    "=== DETTAGLI ANIMALE ===\n\n" +
                            "Chip: %s\n" +
                            "Nome: %s\n" +
                            "Razza: %s\n" +
                            "Colore: %s\n" +
                            "Data di Nascita: %s\n" +
                            "Proprietario: %s",
                    animal.getChip(),
                    animal.getNome(),
                    animal.getRazza(),
                    animal.getColore(),
                    formatDate(animal.getDataNascita()),
                    username
            );

            JTextArea textArea = new JTextArea(details);
            textArea.setEditable(false);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 250));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Dettagli: " + animal.getNome(),
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void exportToCSV() {
            if (filteredAnimals.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessun dato da esportare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salva file CSV");
            fileChooser.setSelectedFile(new File("animali_" + username + ".csv"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("File CSV (*.csv)", "csv");
            fileChooser.setFileFilter(filter);

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new File(file.getAbsolutePath() + ".csv");
                }

                try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
                    // Header
                    pw.println("Numero,Chip,Nome,Razza,Colore,Data Nascita,Proprietario");

                    // Dati (esporta tutti gli animali filtrati, non solo la pagina corrente)
                    for (int i = 0; i < filteredAnimals.size(); i++) {
                        Animale animal = filteredAnimals.get(i);
                        pw.printf("%d,%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                                i + 1,
                                animal.getChip(), // Il chip è già un int, non serve escapeCsv
                                escapeCsv(animal.getNome()),
                                escapeCsv(animal.getRazza()),
                                escapeCsv(animal.getColore()),
                                formatDate(animal.getDataNascita()),
                                escapeCsv(username)
                        );
                    }

                    JOptionPane.showMessageDialog(this,
                            String.format("Esportati %d animali nel file:\n%s", filteredAnimals.size(), file.getAbsolutePath()),
                            "Esportazione Completata",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Errore durante l'esportazione:\n" + ex.getMessage(),
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private String escapeCsv(String value) {
            if (value == null) return "";
            return value.replace("\"", "\"\""); // Escape delle virgolette per CSV
        }

        private String formatDate(Date date) {
            if (date == null) return "Non specificata";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(date);
        }
    }
}
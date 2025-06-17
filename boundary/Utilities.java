package boundary;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;

public class Utilities {

    public final Color Blue = new Color(70, 130, 180);
    public final Color Red = new Color(220, 20, 60);
    public final Color Green = new Color(34, 139, 34);
    public final Color LightGray = new Color(245, 245, 245);
    public final Color DarkGray = new Color(169, 169, 169);
    public final Color Orange = new Color(255, 165, 0);

    public static final Color CALENDAR_HEADER_COLOR = new Color(70, 130, 180);
    public static final Color CALENDAR_BACKGROUND = new Color(245, 250, 255);
    public static final Color SELECTED_DAY_COLOR = new Color(70, 130, 180);

    public final String[] animalTypes = {
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
    };

    public final String[] visitType = {
            "Vaccinazione",
            "Controllo",
            "Intervento chirurgico"
    };

    public Utilities() {
    }

    // Funzione per creare bottoni tutti uguali
    public JButton createButton(String text, Color bgColor) {
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

    // funzione per visualizzare un messaggio di errore
    public void showErrorMessage(JPanel panel,String message) {
        JOptionPane.showMessageDialog(panel, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel createSectionPanel(String title) {
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

    /**
     * Crea un pannello con un calendario avanzato
     * @param dateLabel JLabel che mostrerà la data selezionata
     * @return JPanel contenente il calendario
     */
    // Versione con callback
    public static JPanel createAdvancedCalendar(JLabel dateLabel, Consumer<Date> dateSelectionCallback) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Seleziona Data"));
        panel.setBackground(CALENDAR_BACKGROUND);

        // Header con mese e anno
        JPanel headerPanel = new JPanel(new FlowLayout());
        headerPanel.setBackground(CALENDAR_BACKGROUND);

        Calendar cal = Calendar.getInstance();

        JButton prevButton = createCalendarNavButton("<");
        JButton nextButton = createCalendarNavButton(">");
        JLabel meseAnnoLabel = new JLabel();

        updateMeseAnnoLabel(meseAnnoLabel, cal);

        headerPanel.add(prevButton);
        headerPanel.add(meseAnnoLabel);
        headerPanel.add(nextButton);

        // Griglia giorni
        JPanel giorniPanel = new JPanel(new GridLayout(7, 7, 2, 2));
        giorniPanel.setBackground(CALENDAR_BACKGROUND);

        // Header giorni settimana
        String[] giorni = {"Dom", "Lun", "Mar", "Mer", "Gio", "Ven", "Sab"};
        for (String giorno : giorni) {
            JLabel label = new JLabel(giorno, JLabel.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(CALENDAR_HEADER_COLOR);
            giorniPanel.add(label);
        }

        // Bottoni per i giorni
        JButton[][] giorniButtons = new JButton[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                giorniButtons[i][j] = new JButton();
                giorniButtons[i][j].setPreferredSize(new Dimension(40, 40));
                giorniButtons[i][j].setBackground(Color.WHITE);
                giorniButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                giorniPanel.add(giorniButtons[i][j]);
            }
        }

        // Popola calendario
        Runnable updateCalendar = () -> {
            updateMeseAnnoLabel(meseAnnoLabel, cal);
            populateCalendar(giorniButtons, cal);
        };

        // Aggiungi listener per la selezione della data
        setupDaySelectionListeners(giorniButtons, cal, dateLabel, dateSelectionCallback);

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

    private static JButton createCalendarNavButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(60, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return button;
    }

    private static void updateMeseAnnoLabel(JLabel label, Calendar cal) {
        String[] mesi = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
        label.setText(mesi[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    private static void populateCalendar(JButton[][] buttons, Calendar cal) {
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

    private static void setupDaySelectionListeners(JButton[][] buttons, Calendar cal,
                                                   JLabel dateLabel, Consumer<Date> onDateSelected) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                final int row = i, col = j;
                buttons[i][j].addActionListener(e -> {
                    // Reset colori
                    for (int x = 0; x < 6; x++) {
                        for (int y = 0; y < 7; y++) {
                            buttons[x][y].setBackground(Color.WHITE);
                            buttons[x][y].setForeground(Color.BLACK);
                        }
                    }

                    // Evidenzia selezionato
                    buttons[row][col].setBackground(SELECTED_DAY_COLOR);
                    buttons[row][col].setForeground(Color.WHITE);

                    // Calcola data selezionata
                    String giornoText = buttons[row][col].getText();
                    if (!giornoText.isEmpty()) {
                        try {
                            int giorno = Integer.parseInt(giornoText);
                            Calendar newCal = (Calendar) cal.clone();
                            newCal.set(Calendar.DAY_OF_MONTH, giorno);
                            Date selectedDate = newCal.getTime();

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            dateLabel.setText("Data selezionata: " + sdf.format(selectedDate));

                            // Chiama il callback con la nuova data selezionata
                            onDateSelected.accept(selectedDate);
                        } catch (NumberFormatException ignored) {}
                    }
                });
            }
        }
    }
}


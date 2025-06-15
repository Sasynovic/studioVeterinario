package boundary;

import javax.swing.*;
import java.awt.*;

public class Utilities {

    public final Color Blue = new Color(70, 130, 180);
    public final Color Red = new Color(220, 20, 60);
    public final Color Green = new Color(34, 139, 34);
    public final Color LightGray = new Color(245, 245, 245);
    public final Color DarkGray = new Color(169, 169, 169);
    public final Color Orange = new Color(255, 165, 0);

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
}

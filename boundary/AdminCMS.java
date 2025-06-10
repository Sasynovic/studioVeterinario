package boundary;

import javax.swing.*;
import java.awt.*;

public class AdminCMS {
    private JPanel adminPanel;
    private JButton elencoVisiteGiornaliereButton;
    private JButton visualizzaAnimaliUltimaVaccinazioneButton;
    private JButton inserisciSlotButton;
    private JButton logoutButton;
    private JLabel titleLabel;

    public AdminCMS(JFrame frame) {
        // Inizializzazione pannello principale
        adminPanel = new JPanel();
        adminPanel.setBackground(new Color(245, 250, 255));
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        adminPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));


        // Titolo
        titleLabel = new JLabel("Pannello Amministratore");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        // Pulsante Elenco Visite Giornaliere
        elencoVisiteGiornaliereButton = new JButton("Elenco Visite Giornaliere");
        styleButton(elencoVisiteGiornaliereButton, new Color(70, 130, 180));

        // Pulsante Visualizza Animali Ultima Vaccinazione
        visualizzaAnimaliUltimaVaccinazioneButton = new JButton("Animali da Vaccinare");
        styleButton(visualizzaAnimaliUltimaVaccinazioneButton, new Color(60, 179, 113));

        // Pulsante Inserisci Disponibilità
        inserisciSlotButton = new JButton("Inserisci Disponibilità");
        styleButton(inserisciSlotButton, new Color(186, 85, 211));


        // Pulsante Logout
        logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(169, 169, 169));

        // Aggiunta componenti
        adminPanel.add(titleLabel);
        adminPanel.add(Box.createVerticalStrut(15));
        adminPanel.add(elencoVisiteGiornaliereButton);
        adminPanel.add(Box.createVerticalStrut(10));
        adminPanel.add(visualizzaAnimaliUltimaVaccinazioneButton);
        adminPanel.add(Box.createVerticalStrut(10));
        adminPanel.add(inserisciSlotButton);
        adminPanel.add(Box.createVerticalStrut(10));
        adminPanel.add(logoutButton);

        // Eventi
        elencoVisiteGiornaliereButton.addActionListener(e -> {
            // Apri la finestra per visualizzare le visite giornaliere
            new VisiteGiornaliereDialog(frame).setVisible(true);
        });

        visualizzaAnimaliUltimaVaccinazioneButton.addActionListener(e -> {
            // Apri la finestra per gli animali da vaccinare
//            new AnimaliVaccinazioneDialog(frame).setVisible(true);
        });

        inserisciSlotButton.addActionListener(e -> {
            // Apri la finestra per inserire disponibilità
//            new DisponibilitaDialog(frame).setVisible(true);
        });


        logoutButton.addActionListener(e -> {
            // Torna alla homepage
            frame.setContentPane(new Homepage().getHomepagePanel());
            frame.revalidate();
            frame.repaint();
        });
    }

    public JPanel getAdminPanel() {
        return adminPanel;
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(250, 45));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Classe interna per il dialog delle visite giornaliere (esempio)
    private static class VisiteGiornaliereDialog extends JDialog {
        public VisiteGiornaliereDialog(JFrame parent) {
            super(parent, "Elenco Visite Giornaliere", true);
            setSize(600, 400);
            setLocationRelativeTo(parent);


        }
    }

    // Altre classi interne per i dialog...
}
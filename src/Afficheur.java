import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Afficheur extends JFrame {

    private final List<Bin> bins;

    public Afficheur(List<Bin> bins) {
        this.bins = bins;
        setTitle("Afficheur de Bins");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        afficherBins();

        setVisible(true);
    }

    private void afficherBins() {
        JPanel mainPanel = new JPanel(); // Panneau principal pour tous les bins
        mainPanel.setLayout(new FlowLayout());
        mainPanel.setPreferredSize(new Dimension(800, 600)); // Taille de la fenêtre principale

        for (Bin bin : bins) {
            JPanel binPanel = new JPanel();
            binPanel.setLayout(null); // Utiliser un layout null pour positionner les sous-bins manuellement
            binPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            binPanel.setPreferredSize(new Dimension(bin.getWidth(), bin.getHeight())); // Taille des bins initiaux

            afficherSousBinsRecursif(bin, binPanel);

            mainPanel.add(binPanel);
        }

        setContentPane(mainPanel);
        revalidate(); // Rafraîchit l'affichage
    }

    private void afficherSousBinsRecursif(Bin bin, JPanel parentPanel) {
        List<Bin> sousBins = bin.getSousBins();
        int currentX = 0;
        int maxY = 0; // Variable pour suivre la hauteur maximale des sous-bins
        for (int i = 0; i < sousBins.size(); i++) {
            Bin sousBin = sousBins.get(i);
            JPanel sousBinPanel = new JPanel();
            sousBinPanel.setLayout(null); // Utiliser un layout null pour positionner les sous-bins manuellement
            sousBinPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
            sousBinPanel.setPreferredSize(new Dimension(sousBin.getWidth(), sousBin.getHeight())); // Taille des sous-bins

            if (sousBin.getItem() != null) {
                // Colorier l'intérieur du sous-bin pour représenter l'item
                sousBinPanel.setBackground(sousBin.getItem().getColor());
            }

            // Positionner les sous-bins côte à côte
            sousBinPanel.setBounds(currentX, 0, sousBin.getWidth(), sousBin.getHeight());
            parentPanel.add(sousBinPanel);

            // Mettre à jour la hauteur maximale
            maxY = Math.max(maxY, sousBin.getHeight());

            // Si ce n'est pas le dernier sous-bin, ajouter la largeur au currentX
            if (i < sousBins.size() - 1) {
                currentX += sousBin.getWidth();
            } else {
                // Si c'est le dernier sous-bin, réinitialiser currentX et positionner en bas
                currentX = 0;
                maxY = bin.getHeight()- sousBin.getHeight(); // Augmenter la hauteur maximale pour le sous-bin en bas
                sousBinPanel.setBounds(currentX, maxY, sousBin.getWidth(), sousBin.getHeight());

            }

            // Si le sous-bin a des sous-bins, afficher récursivement les items à l'intérieur
            if (!sousBin.getSousBins().isEmpty()) {
                afficherSousBinsRecursif(sousBin, sousBinPanel);
            }
        }
    }
}

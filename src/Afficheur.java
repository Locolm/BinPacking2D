
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

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
/*
    private void afficherBins() {
        for (Bin bin : bins) {
            afficherSousBinsRecursif(bin, bin.width, bin.height);
        }
    }

    private void afficherSousBinsRecursif(Bin bin, int parentWidth, int parentHeight) {
        JPanel binPanel = new JPanel();
        binPanel.setLayout(new GridLayout(2, 2)); // GridLayout pour afficher les sous-bins
        binPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        binPanel.setPreferredSize(new Dimension(parentWidth, parentHeight));

        List<Bin> sousBins = bin.sousBins;
        for (Bin sousBin : sousBins) {
            JPanel sousBinPanel = new JPanel();
            sousBinPanel.setLayout(new FlowLayout());
            sousBinPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
            sousBinPanel.setPreferredSize(new Dimension(sousBin.width, sousBin.height)); // Taille des sous-bins

            if (sousBin.item != null) {
                JPanel itemPanel = new JPanel();
                itemPanel.setPreferredSize(new Dimension(sousBin.item.getWidth(), sousBin.item.getHeight()));
                itemPanel.setBackground(generateRandomColor());
                sousBinPanel.add(itemPanel);
            }

            binPanel.add(sousBinPanel);

            // Vérifier s'il y a des sous-bins pour ce sous-bin et les parcourir récursivement
            if (!sousBin.sousBins.isEmpty()) {
                afficherSousBinsRecursif(sousBin, sousBin.width, sousBin.height);
            }
        }

        add(binPanel);
    }
 */

    private void afficherBins() {
        JPanel mainPanel = new JPanel(); // Panneau principal pour tous les bins
        mainPanel.setLayout(new FlowLayout());
        mainPanel.setPreferredSize(new Dimension(800, 600)); // Taille de la fenêtre principale

        for (Bin bin : bins) {
            JPanel binPanel = new JPanel();
            binPanel.setLayout(new GridLayout(2, 2)); // GridLayout pour afficher les sous-bins
            binPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            binPanel.setPreferredSize(new Dimension(bin.width, bin.height)); // Taille des bins initiaux

            afficherSousBinsRecursif(bin, binPanel);

            mainPanel.add(binPanel);
        }

        setContentPane(mainPanel);
        revalidate(); // Rafraîchit l'affichage
    }

    private void afficherSousBinsRecursif(Bin bin, JPanel parentPanel) {
        List<Bin> sousBins = bin.sousBins;
        for (Bin sousBin : sousBins) {
            JPanel sousBinPanel = new JPanel();
            sousBinPanel.setLayout(new FlowLayout());
            sousBinPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
            sousBinPanel.setPreferredSize(new Dimension(sousBin.width, sousBin.height)); // Taille des sous-bins

            if (sousBin.item != null) {
                JPanel itemPanel = new JPanel();
                itemPanel.setPreferredSize(new Dimension(sousBin.item.getWidth(), sousBin.item.getHeight()));
                itemPanel.setBackground(generateRandomColor());
                sousBinPanel.add(itemPanel);
            }

            parentPanel.add(sousBinPanel);

            // Vérifier s'il y a des sous-bins pour ce sous-bin et les parcourir récursivement
            if (!sousBin.sousBins.isEmpty()) {
                afficherSousBinsRecursif(sousBin, sousBinPanel);
            }
        }
    }



    private Color generateRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
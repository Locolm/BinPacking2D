import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Afficheur extends JFrame {

    private final List<Bin> bins;

    private JPanel[] binsPanels;

    private double ratio;

    public Afficheur(List<Bin> bins) {
        this.bins = bins;
        this.binsPanels = new JPanel[bins.size()];
        if (bins.getFirst().height <= 250) {
            if (bins.size()>=19){ratio = 0.8;}
            else {ratio = 1.0;}
        } else if (bins.getFirst().height >= 1000) {
            if (bins.size()>=3){ratio = 0.1;}
            else {ratio = 0.2;}
        } else if (bins.getFirst().height >= 500) {
            if (bins.size()>=19){ratio = 0.2;}
            else if(bins.size()<=8) {ratio = 0.7;}
        }
        else {
            ratio = 0.5;
        }
    }
    public void display() {
        setTitle("Afficheur de Bins");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        // Création de la barre de menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Création du menu "Options"
        JMenu menuOptions = new JMenu("Options");
        menuBar.add(menuOptions);

        // Ajout des boutons
        JMenuItem zoomPlus = new JMenuItem("+");
        zoomPlus.addActionListener(e -> {
            if (ratio + 0.1 <= 1)
            {
                ratio = Math.round((ratio + 0.1) * 10.0) / 10.0;
                System.out.println(ratio);
                afficherBins();
                revalidate();
                repaint();
            }
        });
        menuOptions.add(zoomPlus);

        JMenuItem zoomMinus = new JMenuItem("-");
        zoomMinus.addActionListener(e -> {
            if (ratio - 0.1 > 0)
            {
                ratio = Math.round((ratio - 0.1) * 10.0) / 10.0;
                System.out.println(ratio);
                afficherBins();
                revalidate();
                repaint();
            }

        });
        menuOptions.add(zoomMinus);

        afficherBins();

        setVisible(true);
    }

    private void afficherBins() {
        JPanel mainPanel = new JPanel(); // Panneau principal pour tous les bins
        mainPanel.setLayout(new FlowLayout());

        for (int i = 0; i < bins.size(); i++) {
            Bin bin= bins.get(i);
            JPanel binPanel = new JPanel();
            binPanel.setLayout(null); // Utiliser un layout null pour positionner les sous-bins manuellement
            binPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            binPanel.setPreferredSize(new Dimension( (int) (bin.getWidth()*ratio) , (int) (bin.getHeight()*ratio))); // Taille des bins initiaux

            afficherSousBinsRecursif(bin, binPanel);

            binsPanels[i]=(binPanel);
            mainPanel.add(binPanel);
        }

        setContentPane(mainPanel);
        revalidate(); // Rafraîchit l'affichage
    }

    public void refreshBin(int index) {
        if (index < 0 || index >= bins.size()) {
            throw new IndexOutOfBoundsException("Index de bin invalide");
        }

        Bin bin = bins.get(index);

        JPanel binPanel = binsPanels[index];
        binPanel.removeAll();
        binPanel.setPreferredSize(new Dimension((int) (bin.getWidth() * ratio), (int) (bin.getHeight() * ratio)));

        afficherSousBinsRecursif(bin, binPanel);

        revalidate();
        repaint();
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
            sousBinPanel.setPreferredSize(new Dimension((int) (sousBin.getWidth() * ratio), (int) (sousBin.getHeight() * ratio))); // Taille des sous-bins

            if (sousBin.getItem() != null) {
                // Colorier l'intérieur du sous-bin pour représenter l'item
                sousBinPanel.setBackground(sousBin.getItem().getColor());
            }

            // Positionner les sous-bins côte à côte
            sousBinPanel.setBounds(currentX, 0, (int) (sousBin.getWidth() * ratio), (int) (sousBin.getHeight() * ratio));
            parentPanel.add(sousBinPanel);

            // Mettre à jour la hauteur maximale
            maxY = Math.max(maxY, (int) (sousBin.getHeight() * ratio));

            // Si ce n'est pas le dernier sous-bin, ajouter la largeur au currentX
            if (i < sousBins.size() - 1) {
                currentX += (int) (sousBin.getWidth() * ratio);
            } else {
                // Si c'est le dernier sous-bin, réinitialiser currentX et positionner en bas
                currentX = 0;
                maxY = (int) (bin.getHeight() * ratio) - (int) (sousBin.getHeight() * ratio); // Augmenter la hauteur maximale pour le sous-bin en bas
                sousBinPanel.setBounds(currentX, maxY, (int) (sousBin.getWidth() * ratio), (int) (sousBin.getHeight() * ratio));

            }

            // Si le sous-bin a des sous-bins, afficher récursivement les items à l'intérieur
            if (!sousBin.getSousBins().isEmpty()) {
                afficherSousBinsRecursif(sousBin, sousBinPanel);
            }
        }
    }
}

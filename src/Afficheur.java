import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class Afficheur extends JFrame {

    private final List<Bin> bins;

    private double ratio = 0.9;

    public Afficheur(List<Bin> bins) {
        this.bins = bins;
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
        zoomPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ratio + 0.1 <= 1)
                {
                    ratio = Math.round((ratio + 0.1) * 10.0) / 10.0;
                    System.out.println(ratio);
                    afficherBins();
                    revalidate();
                    repaint();
                }
            }
        });
        menuOptions.add(zoomPlus);

        JMenuItem zoomMinus = new JMenuItem("-");
        zoomMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ratio - 0.1 >= 0)
                {
                    ratio = Math.round((ratio - 0.1) * 10.0) / 10.0;
                    System.out.println(ratio);
                    afficherBins();
                    revalidate();
                    repaint();
                }

            }
        });
        menuOptions.add(zoomMinus);

        afficherBins();

        setVisible(true);
    }

    public static int[] trouverFacteursProches(int nombre) {
        int[] facteurs = new int[2];

        // Chercher les deux facteurs les plus proches du carré racine du nombre
        int racine = (int) Math.sqrt(nombre);
        int facteur1 = racine;
        while (nombre % facteur1 != 0) {
            facteur1--;
        }
        int facteur2 = nombre / facteur1;

        facteurs[0] = facteur1;
        facteurs[1] = facteur2;

        // Trier les facteurs dans l'ordre décroissant
        Arrays.sort(facteurs);

        return facteurs ;
    }

    private void afficherBins() {
        JPanel mainPanel = new JPanel(); // Panneau principal pour tous les bins
        mainPanel.setLayout(new FlowLayout());

        for (Bin bin : bins) {
            JPanel binPanel = new JPanel();
            binPanel.setLayout(null); // Utiliser un layout null pour positionner les sous-bins manuellement
            binPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            binPanel.setPreferredSize(new Dimension( (int) (bin.getWidth()*ratio) , (int) (bin.getHeight()*ratio))); // Taille des bins initiaux

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
                currentX += sousBin.getWidth() * ratio;
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

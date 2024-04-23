
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Afficheur extends JFrame {

    private List<Bin> bins;

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

//    private void afficherBins() {
//        for (Bin bin : bins) {
//            JPanel binPanel = new JPanel();
//            binPanel.setLayout(new FlowLayout());
//            binPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//            binPanel.setPreferredSize(new Dimension(bin.width, bin.height));
//
//            if (bin.item != null) {
//                JPanel itemPanel = new JPanel();
//                itemPanel.setPreferredSize(new Dimension(bin.item.getWidth(), bin.item.getHeight()));
//                itemPanel.setBackground(generateRandomColor());
//                binPanel.add(itemPanel);
//            }
//
//            add(binPanel);
//        }
//    }
    private void afficherBins() {
    for (Bin bin : bins) {
        JPanel binPanel = new JPanel();
        binPanel.setLayout(new GridLayout(2, 2)); // GridLayout pour afficher les sous-bins
        binPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        binPanel.setPreferredSize(new Dimension(bin.width, bin.height));

        // Parcourir les sous-bins
        List<Bin> sousBins = bin.sousBins;
        for (int i = 0; i < 4; i++) {
            JPanel sousBinPanel = new JPanel();
            sousBinPanel.setLayout(new FlowLayout());
            sousBinPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
            sousBinPanel.setPreferredSize(new Dimension(bin.sousBins.get(i).width, bin.sousBins.get(i).height));

            if (sousBins.size() > i) {
                Bin sousBin = sousBins.get(i);
                if (sousBin.item != null) {
                    JPanel itemPanel = new JPanel();
                    itemPanel.setPreferredSize(new Dimension(sousBin.item.getWidth(), sousBin.item.getHeight()));
                    itemPanel.setBackground(generateRandomColor());
                    sousBinPanel.add(itemPanel);
                }
            }

            binPanel.add(sousBinPanel);
        }

        add(binPanel);
    }
}

    private Color generateRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
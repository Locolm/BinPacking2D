import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BinPackingSolver {
    private List<Bin> bins;
    private List<Item> items;

    private int binWidth;
    private int binHeight;

    public BinPackingSolver(int binWidth, int binHeight, List<Item> items) {
        this.binWidth = binWidth;
        this.binHeight = binHeight;
        this.bins = new ArrayList<>();
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void solve() {
        // Création du premier bin
        Bin firstBin = new Bin(binWidth, binHeight);
        bins.add(firstBin);

        // Placement des items dans les bins avec rotations si nécessaire
        for (Item item : items) {
            boolean placed = false;
            for (Bin bin : bins) {
                if (bin.addItem(item)) {
                    placed = true;
                    break;
                } else if (bin.addItemWithRotation(item)) {
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                // Si l'item ne peut pas être placé dans les bins existants, on crée un nouveau bin
                Bin newBin = new Bin(binWidth, binHeight);
                newBin.addItem(item);
                bins.add(newBin);
            }
        }

        // Affichage visuel des bins et des items
        display();

    }

    public void display() {
        Afficheur afficheur = new Afficheur(bins);
    }

}

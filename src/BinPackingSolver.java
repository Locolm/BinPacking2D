import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BinPackingSolver {
    private List<Bin> bins;
    private final List<Item> items;

    private List<List<Bin>> solutions;
    private List<Bin> originalSolution;

    private List<Map.Entry<Integer,Bin>> Voisins;

    private final int binWidth;
    private final int binHeight;

    private Afficheur afficheur;

    public BinPackingSolver(int binWidth, int binHeight, List<Item> items) {
        this.binWidth = binWidth;
        this.binHeight = binHeight;
        this.bins = new ArrayList<>();
        this.items = items;
    }

    public void placeItem(List<Item> listItems) {
        // Placement des items dans les bins avec rotations si nécessaire
        // Vérifier chaque item
        for (Item item : listItems) {
            boolean placed = false;
            for (Bin bin : bins) {
                if (bin.addItem(item)) {
                    placed = true;
                } else if (bin.addItemWithRotation(item)) {
                    placed = true;
                }
               if (placed){
                    break;
                }
            }
            if (!placed) {
                // Si l'item ne peut pas être placé dans les bins existants, on crée un nouveau bin
                List<Integer> posBin = new ArrayList<>();
                posBin.add(bins.size()); // car la position commence à 0
                Bin newBin = new Bin(binWidth, binHeight,posBin);
                newBin.addItem(item);
                bins.add(newBin);
            }
        }
    }

    public void init() throws Exception {
        // Création du premier bin
        List<Integer> posBin = new ArrayList<>();
        posBin.add(0);
        Bin firstBin = new Bin(binWidth, binHeight,posBin);
        bins.add(firstBin);
        placeItem(items);

        // Affichage visuel des bins et des items
        display();

        //Tabou
        int iteration = 5;
        boolean displayNeighbour = false;
        int waitingTime=500;
        //parcours des voisins et selection du meilleur dans this.bins
        for(int i=0;i<iteration;i++){
            originalSolution = new ArrayList<>(bins);
            selectAndSwitch(waitingTime,displayNeighbour);
            refreshAll(waitingTime);
        }
    }

    public void switchItem(int ind1, int ind2,int wait, boolean displayNeighbour) {
        if (ind1 < 0 || ind1 >= items.size() || ind2 < 0 || ind2 >= items.size()) {
            throw new IndexOutOfBoundsException("Indices are out of bounds");
        }
        Item temp = items.get(ind1);
        items.set(ind1, items.get(ind2));
        items.set(ind2, temp);
        //reset bins
        this.bins = new ArrayList<>();
        List<Integer> posBin = new ArrayList<>();
        posBin.add(0);
        Bin firstBin = new Bin(binWidth, binHeight,posBin);
        this.bins.add(firstBin);
        //replace items
        placeItem(items);
        if (displayNeighbour){refreshAll(wait);}
    }

    public void selectAndSwitch( int wait, boolean displayNeighbour ) throws Exception {
        for (int i = 0; i < bins.size(); i++) {
            for (int j = i + 1; j < bins.size() - 1; j++) {
                if(!Objects.equals(items.get(i).position.getFirst(), items.get(j).position.getFirst())) {
                    switchItem(i,j,wait,displayNeighbour);
                    if(originalSolution.size()>=bins.size()){
                        originalSolution = bins;
                    }
                }
            }
        }
        bins = originalSolution;
    }

    public void display() {
        afficheur = new Afficheur(bins);
        afficheur.display();
    }

    public void refreshAll(int wait){
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i =0; i<bins.size();i++){
            afficheur.refreshBin(i,bins);
        }
    }

}


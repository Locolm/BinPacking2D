import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BinPackingSolver {
    private List<Bin> bins;
    private List<Item> items;

    //Tabou
    private  List<Pair<Pair<Integer, Integer>, List<Bin>>> solutions; //pour évaluer si dégradation
    private List<List<Item>> listItemsSolutions;
    private List<Bin> originalSolution;
    private List<Pair<Integer, Integer>> tabou = new ArrayList<>();
    //asked user
    private int lengthTabou=10;
    int iteration = 5;
    boolean displayNeighbour = false;
    int waitingTime=500;

    private final int binWidth;
    private final int binHeight;

    private Afficheur afficheur;

    public BinPackingSolver(int binWidth, int binHeight, List<Item> items,int iteration,int lengthTabou, int waitingTime,boolean displayNeighbour) {
        this.binWidth = binWidth;
        this.binHeight = binHeight;
        this.bins = new ArrayList<>();
        this.items = items;
        this.solutions = new ArrayList<>();
        this.lengthTabou=lengthTabou;
        this.iteration = iteration;
        this.displayNeighbour = displayNeighbour;
        this.waitingTime=waitingTime;
        this.listItemsSolutions = new ArrayList<>();
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
        System.out.println("you have 5 second to open the window");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Running tabou ...");
        //Tabou
        //parcours des voisins et selection du meilleur dans this.bins
        for(int i=0;i<iteration;i++){
            originalSolution = new ArrayList<>(bins);
            selectAndSwitch(waitingTime,displayNeighbour);
            bins = new ArrayList<>();
            placeItem(items);
            refreshAll(waitingTime);
        }
    }

    public List<Item> switchItem(int ind1, int ind2,int wait, boolean displayNeighbour) {
        List<Item> newListItems = new ArrayList<>(items);
        if (ind1 < 0 || ind1 >= newListItems.size() || ind2 < 0 || ind2 >= newListItems.size()) {
            throw new IndexOutOfBoundsException("Indices are out of bounds");
        }
        Item temp = newListItems.get(ind1);
        newListItems.set(ind1, newListItems.get(ind2));
        newListItems.set(ind2, temp);
        //reset bins
        this.bins = new ArrayList<>();
        List<Integer> posBin = new ArrayList<>();
        posBin.add(0);
        Bin firstBin = new Bin(binWidth, binHeight,posBin);
        this.bins.add(firstBin);
        //replace items
        placeItem(newListItems);
        if (displayNeighbour){refreshAll(wait);}
        return newListItems;
    }

    public boolean checkTabou(int i, int j) {
        for (Pair<Integer, Integer> pair : tabou) {
            if (Objects.equals(pair.getFirst(), i) && Objects.equals(pair.getSecond(), j)) {
                return true;
            }
        }
        return false;
    }

    public void selectAndSwitch( int wait, boolean displayNeighbour ) throws Exception {
        boolean switched = false;
        int count= 0;
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size() - 1; j++) {
                listItemsSolutions.add(switchItem(i,j,wait,displayNeighbour));
                //si cela ne correspond pas à un changement de tabou et que l'on respecte les conditions
                if(originalSolution.size()>=bins.size() && !checkTabou(i,j)){
                    //on choisi cette solution comme optimale
                    originalSolution = bins;
                    switched = true;
                    count++;
                }
                solutions.add(new Pair<>(new Pair<>(i, j), bins));
            }
        }
        if (!switched){
            //si on arrive là c'est que l'on a dégradation de la solution
            //on prend la meilleure solution possible
            int minBins = solutions.getFirst().getSecond().size();
            List<Pair<Integer,Integer>> changeItem = new ArrayList<>();
            for (int i=0;i<solutions.size();i++) {
                Pair<Pair<Integer, Integer>, List<Bin>> solution = solutions.get(i);
                //si la solution ne correspond pas à un changement de tabou et qu'on respecte les conditions
                if (minBins >= solution.getSecond().size() && !checkTabou(solution.getFirst().getFirst(),solution.getFirst().getSecond())) {
                    minBins = solution.getSecond().size();
                    originalSolution = solution.getSecond();
                    items = listItemsSolutions.get(i);
                    if (changeItem.isEmpty()){
                        changeItem = new ArrayList<>();
                    }
                    changeItem.add(solution.getFirst());
                }
            }
            //s'il y a bien eu un changement
            if (!changeItem.isEmpty()) {
                //on ajoute la dégradation à la liste tabou
                if (tabou.size() >= lengthTabou) {
                    tabou.removeFirst();
                    tabou.add(changeItem.getFirst());
                } else {
                    tabou.add(changeItem.getFirst());
                }
            }
        }
        else {
            items = listItemsSolutions.get(count-1);
            bins = originalSolution;
        }

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


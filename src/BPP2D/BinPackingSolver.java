package BPP2D;

import App.Afficheur;
import Metaheuristics.Metaheuristic;
import Utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BinPackingSolver {
    public Metaheuristic metaheuristic;
    public List<Bin> bins;
    public List<Item> items;
    public int bestEval;

    //Tabou
    public  List<Pair<Pair<Integer, Integer>, List<Bin>>> solutions; //pour évaluer si dégradation
    public List<List<Item>> listItemsSolutions;
    public List<Bin> originalSolution;
    public List<Pair<Integer, Integer>> tabou = new ArrayList<>();

    // Record best solution so far
    public List<Bin> bestSolution;


    private final int binWidth;
    private final int binHeight;

    private Afficheur afficheur;

    public BinPackingSolver(int binWidth, int binHeight, List<Item> items, Metaheuristic metaheuristic){
        this.binWidth = binWidth;
        this.binHeight = binHeight;
        this.bins = new ArrayList<>();
        this.items = items;
        this.solutions = new ArrayList<>();
        this.listItemsSolutions = new ArrayList<>();
        this.metaheuristic = metaheuristic;
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
        bestSolution = new ArrayList<>(bins);

        // Affichage visuel des bins et des items
        display();
        System.out.println("you have 5 second to open the window");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.metaheuristic.run(this);
    }

    public List<Item> switchItem(int ind1, int ind2) {
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

    /**
     * Selectionne et switch les items  selon Tabou
     */
    public void selectAndSwitch(int lengthTabou ) throws Exception {
        boolean switched = false;
        int count= 0;
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size() - 1; j++) {
                listItemsSolutions.add(switchItem(i,j));
                //si cela ne correspond pas à un changement de tabou et que l'on respecte les conditions
                if(originalSolution.size()>=bins.size() && !checkTabou(i,j)){
                    //on choisi cette solution comme optimale
                    originalSolution = bins;
                    switched = true;
                    count++;
                }
                // Update best solution
                if (bins.size() < bestSolution.size()) {
                    bestSolution = new ArrayList<>(bins);
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

    public void selectAndSwitchRandom() throws Exception {
        int i = (int) (Math.random() * items.size());
        int j = (int) (Math.random() * items.size());
        while (i == j) {
            j = (int) (Math.random() * items.size());
        }
        switchItem(i, j);

        // Update best solution
        if (bins.size() < bestSolution.size()) {
            bestSolution = new ArrayList<>(bins);
        }
    }

    public void display() {
        afficheur = new Afficheur(bins);
        afficheur.display();
    }

    public void refreshAll(int wait){
        if (wait > 0){
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i =0; i<bins.size();i++){
            afficheur.refreshBin(i,bins);
        }
    }

    public void displayBestSolution(int wait){
        if (wait > 0){
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i =0; i<bestSolution.size();i++){
            afficheur.refreshBin(i,bestSolution);
        }
    }

    public int eval(List<Bin> bins){
        return bins.size();
    }
}


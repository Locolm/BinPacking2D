import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BinPackingSolver {
    private final List<Bin> bins;
    private final List<Item> items;

    private List<Map.Entry<Integer,Bin>> Voisins;

    private final int binWidth;
    private final int binHeight;

    private List<Integer> position;

    private Afficheur afficheur;

    public BinPackingSolver(int binWidth, int binHeight, List<Item> items) {
        this.binWidth = binWidth;
        this.binHeight = binHeight;
        this.bins = new ArrayList<>();
        this.items = items;
        this.position= new ArrayList<>();
    }

    public void placeItem(List<Item> listItems) {
        // Placement des items dans les bins avec rotations si nécessaire
        for (Item item : listItems) {
            boolean placed = false;
            this.position.set(0, 0);
            for (Bin bin : bins) {
                if (bin.addItem(item, position)) {
                    placed = true;
                } else if (bin.addItemWithRotation(item, position)) {
                    placed = true;
                }
                this.position.set(0, position.getFirst()+1);
            }
            if (!placed) {
                // Si l'item ne peut pas être placé dans les bins existants, on crée un nouveau bin
                Bin newBin = new Bin(binWidth, binHeight);
                newBin.addItem(item, position);
                bins.add(newBin);
            }
        }

    }

    public void init() {
        // Création du premier bin
        Bin firstBin = new Bin(binWidth, binHeight);
        bins.add(firstBin);
        this.position.add(0);

        placeItem(items);

        // Affichage visuel des bins et des items
        display();
        /*
        //faire une boucle et enlever le premier item croisé
        for (Bin sousBin : bins.get(1).sousBins) {
            if (sousBin.item != null) {
                //enlever l'item
                sousBin.item = null;
                break;
            }
        }
        //attendre 1 seconde
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        afficheur.refreshBin(1);*/

    }

    public void iterate(int iterations) {
        for (int i = 0; i < iterations; i++) {
            // Calculer la nouvelle solution
            calculateNewSolution();
            // Rafraîchir l'affichage
            refreshDisplay();
        }
    }

    private void parcoursSousBinsItems(Bin bin, Integer id) {
        //référencer la position des items par levels de sous-bins 0 1 2 dans la liste des items, afin de ne pas avoir à reboucler pour retrouver ceux à échanger.
    for (Bin sousBin : bin.sousBins){
        if (id!=null){
            if(sousBin.item==null) {//échanger l'item avec une place vide (ajout dans la liste des voisins)
                //(voisins calcul d'évaluation de la solution)
                //vérifier si il y a suffisamment de place dans les deux bins pour l'échange
            }
            else {
                //échanger deux items (ajout dans la liste des voisins)
                //(voisins calcul d'évaluation de la solution)
                //vérifier si il y a suffisamment de place dans les deux bins pour l'échange
            }

        }
        else {
            //l'id est null, il faut sélectionner le premier item à échanger
            if (sousBin.item != null) {
                id = sousBin.item.id;
            }
        }
        if (!sousBin.sousBins.isEmpty()){
            for (Bin sousSousBin : sousBin.sousBins) {parcoursSousBinsItems(sousSousBin, id);}
        }

    }
    }

    private void calculateNewSolution() {
        // Implémentez ici votre algorithme pour calculer une nouvelle solution
        // Par exemple, utilisez un algorithme de recherche locale pour améliorer la solution actuelle

        // Parcourir chaque bin
        for (Bin bin : bins) {
            // Parcourir chaque item dans le bin
            parcoursSousBinsItems(bin, null);
                // Chercher les voisins possibles
            }
    }

    public void display() {
        afficheur = new Afficheur(bins);
        afficheur.display();
    }

    private void refreshDisplay() {
        afficheur = new Afficheur(bins);
        afficheur.display();
    }

}

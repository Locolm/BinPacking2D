import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BinPackingSolver {
    private List<Bin> bins;
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

    public void placeItem(List<Item> listItems, List<Bin> bins) {
        boolean isSpecificBin=true;
        if (bins==null){
            bins = this.bins;
            isSpecificBin = false;
        }
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
                List<Item> itemNotPlaced = new ArrayList<>();
                itemNotPlaced.add(item);
                if (isSpecificBin){placeItem(itemNotPlaced,null);}

            }
        }
        this.bins = bins;
    }

    public void init() {
        // Création du premier bin
        Bin firstBin = new Bin(binWidth, binHeight);
        bins.add(firstBin);
        this.position.add(0);

        placeItem(items,null);

        // Affichage visuel des bins et des items
        display();
        selectAndSwitch();
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

    public void guessBestNeighbour(){
        for (int i = 0; i < items.size()-1; i++)
        //neighbourhood search
        {
            for (int j = i+1; j < items.size(); j++)
            {
                //récupérer les ids des items dans les mêmes sousBins des items i et j qui sont ceux qui vont être inversés
                //on vide les sousBins correspondants
                List<Integer> posI = items.get(i).position;
                List<Integer> remainsPosI = posI.subList(1, posI.size());
                List<Item> ItemsI = bins.get(posI.getFirst()).getItemsInSousBins(bins.get(posI.getFirst()).sousBins);
                bins.get(posI.getFirst()).setSousBins(null,remainsPosI);

                List<Integer> posJ = items.get(j).position;
                List<Integer> remainsPosJ = posJ.subList(1, posJ.size());
                List<Item> ItemsJ = bins.get(posJ.getFirst()).getItemsInSousBins(bins.get(posJ.getFirst()).sousBins);
                bins.get(posJ.getFirst()).setSousBins(null,remainsPosJ);

                //on replace ensuite les items dans l'ordre (en inversant I et J)
                // on utilise la fonction de remplissage automatique pour placer les items, dans un bin, s'il n'y a plus de place
                //on crée un nouveau bin
            }
        }
    }

    public void selectAndSwitch(){
        for (int i = 0; i < bins.size(); i++) {
            for (int j = i + 1; j < bins.size() - 1; j++) {
                // On récupère la listes des items de chaque bins
                List<Item> firstListItems = bins.get(i).getItemsInSousBins(bins.get(i).sousBins);
                List<Item> secondListItems = bins.get(j).getItemsInSousBins(bins.get(j).sousBins);
                for (int k = 0; k < firstListItems.size(); k++) {
                    for (int p = 0; p < secondListItems.size(); p++) {
                        //on récupère la liste des items contenant l'item à échanger et ceux placés dans les sousbin associés
                        List<Integer> firstPos = firstListItems.get(k).position;
                        List<Integer> secondPos = secondListItems.get(p).position;

                        // On supprime les sousBins associés (retirant ainsi les items)
                        ResultBin firstBin = BinProcessor.removeBinAtPosition(bins.get(firstPos.getFirst()),firstPos.subList(1,firstPos.size()));
                        ResultBin secondBin = BinProcessor.removeBinAtPosition(bins.get(secondPos.getFirst()),secondPos.subList(1,secondPos.size()));

                        //on replace la liste des items en inversant les bins
                        //on place d'abord item1 puis item2
                        List<Bin> singleFirstBin = new ArrayList<>();
                        singleFirstBin.add(firstBin.bin);
                        List<Bin> signleSecondBin = new ArrayList<>();
                        signleSecondBin.add(secondBin.bin);
                        List<Item> singleFirstItem = new ArrayList<>();
                        singleFirstItem.add(firstBin.items.getFirst());
                        List<Item> singleSecondItem = new ArrayList<>();
                        singleSecondItem.add(firstBin.items.getFirst());
                        placeItem(singleFirstItem, singleFirstBin);
                        placeItem(singleSecondItem, signleSecondBin);
                        //puis on fusionne la liste restante list1+list2
                        List<Item> list1 = firstBin.items.subList(1,firstBin.items.size());
                        List<Item> list2 = secondBin.items.subList(1,secondBin.items.size());
                        list1.addAll(list2);

                        //on utilise la fonction placeItem par défaut pour combler les trous
                        placeItem(list1,null);

                        //on refresh la fenêtre
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        afficheur.refreshBin(i);
                        afficheur.refreshBin(j);

                        //on évalue la solution, si elle est < à min alors on enregistre le nv min
                    }
                }
            }
        }
        // this.bin prends la valeur de la nouvelle solution
        //on ajoute l'interdiction à tabou si on a eu dégradation
    }

/*
    public void selectAndSwitch(List<Bin> bins) {
        for (int i = 0; i < bins.size(); i++) {
            for (int j = i + 1; j < bins.size(); j++) {
                List<Bin> binList = new ArrayList<>();
                binList.add(bins.get(i));
                Result firstElement = BinProcessor.findNthElement(binList, i);
                List<Bin> binList2 = new ArrayList<>();
                binList2.add(bins.get(j));
                Result secondElement = BinProcessor.findNthElement(binList2, j);
                if (firstElement.isItem || secondElement.isItem){
                    ResultBin firstBin = BinProcessor.removeBinAtPosition(bins.get(firstElement.position.getFirst()), firstElement.position.subList(1,firstElement.position.size()));
                    ResultBin secondBin= BinProcessor.removeBinAtPosition(bins.get(secondElement.position.getFirst()), firstElement.position.subList(1,secondElement.position.size()));
                    List<Bin> firstListBin = new ArrayList<>();
                    firstListBin.add(firstBin.bin);
                    List<Bin> secondListBin = new ArrayList<>();
                    secondListBin.add(secondBin.bin);
                    placeItem(secondBin.items,firstListBin);
                    placeItem(firstBin.items,secondListBin);
                    //attendre 1 seconde
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    afficheur.refreshBin(firstElement.position.getFirst());
                    afficheur.refreshBin(secondElement.position.getFirst());

                }
            }
        }
    }
*/

    public List<Item> getItemsByIds(List<Integer> ids){
        List<Item> items = new ArrayList<>();
        for (int id : ids) {
            for (Item item : this.items) {
                if (item.getId() == id) {
                    items.add(item);
                    break;
                }
            }
        }
        return items;
    }

    public void display() {
        afficheur = new Afficheur(bins);
        afficheur.display();
    }

}


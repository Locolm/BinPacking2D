import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BinPackingSolver {
    private List<Bin> bins;
    private final List<Item> items;

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
        //selectAndSwitch();
        List<Integer> test1 = new ArrayList<>();
        test1.add(2);
        test1.add(1);
        List<Integer> test2 = new ArrayList<>();
        test2.add(1);
        switchAndRefresh(test1,test2);
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

    public void switchAndRefresh(List<Integer> pos1,List<Integer> pos2) throws Exception {
        // à utiliser sans donner la dernière position (0) dans le cas d'un item.
        if (pos1.isEmpty() || pos2.isEmpty()){
            throw new Exception("On ne peut pas échanger des positions vides");
        }
        if (pos1==pos2){
            throw new Exception("On ne peut pas échanger les mêmes positions");
        }
        Bin bin1 = getSousBinWithPos(pos1);
        Bin bin2 = getSousBinWithPos(pos2);
        Item item1;
        Item item2;
/*        if (bin1.item!=null){
            item1 = bin1.item;
        } else if (bin2.item==null){
            throw new Exception("Au moins une des positions doit correspondre à un item");
        }
        if (bin2.item !=null){
            item2 = bin2.item;
        }*/
        bin1.sousBins = new ArrayList<>(); //when there is sousbins
        bin2.sousBins.set(pos2.getLast(), new Bin(0, 0, new ArrayList<>())); //when there is no sousbins
        //int w2 = bin2.sousBins.get(pos2.getLast()).width;
/*        int h2 = bin2.sousBins.get(pos2.getLast()).height;
        List<Integer> posBin2 = bin2.sousBins.get(pos2.getLast()).position;
        Bin b = bin2.sousBins.get(pos2.getLast());
        b = new Bin(w2,h2,posBin2);*/
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //afficheur.refreshBin(0);
        refreshAll();
    }

    public Bin getSousBinWithPos(List<Integer> pos){
        List<Bin> selectBins = null;
        for (Integer i : pos) {
            if (Objects.equals(i, pos.getLast()) && selectBins != null){
                return selectBins.get(i);
            } else if (Objects.equals(i, pos.getLast()) && selectBins == null){
                return bins.get(i);
            }
            selectBins = bins.get(i).sousBins;
        }
        return null;
    }

    public void selectAndSwitch(){
        for (int i = 0; i < bins.size(); i++) {
            for (int j = i + 1; j < bins.size() - 1; j++) {
                // On récupère la listes des items de chaque bins
                List<Item> firstListItems = bins.get(i).getItemsInSousBins(bins.get(i).sousBins);
                List<Item> secondListItems = bins.get(j).getItemsInSousBins(bins.get(j).sousBins);
                for (Item firstListItem : firstListItems) {
                    for (Item secondListItem : secondListItems) {
                        //on récupère la liste des items contenant l'item à échanger et ceux placés dans les sousbin associés
                        List<Integer> firstPos = firstListItem.position;
                        List<Integer> secondPos = secondListItem.position;

                        // On supprime les sousBins associés (retirant ainsi les items)
                        ResultBin firstBin = BinProcessor.removeBinAtPosition(bins.get(i), firstPos.subList(1, firstPos.size()));
                        ResultBin secondBin = BinProcessor.removeBinAtPosition(bins.get(j), secondPos.subList(1, secondPos.size()));

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
                        placeItem(singleFirstItem);
                        placeItem(singleSecondItem);
                        //puis on fusionne la liste restante list1+list2
                        List<Item> list1 = firstBin.items.subList(1, firstBin.items.size());
                        List<Item> list2 = secondBin.items.subList(1, secondBin.items.size());
                        list1.addAll(list2);

                        //on utilise la fonction placeItem par défaut pour combler les trous
                        placeItem(list1);

                        //on refresh la fenêtre
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        afficheur.refreshBin(i);
                        afficheur.refreshBin(j);
                        //rafraichir tous les bins ici (finalement on utilise directement placeItem
                        //pour combler les trous, donc il y a possibilité que d'autres bins sois changés que ceux concernés par le switch

                        //on évalue la solution, si elle est < à min alors on enregistre le nv min
                    }
                }
            }
        }
        // this.bin prends la valeur de la nouvelle solution
        //on ajoute l'interdiction à tabou si on a eu dégradation
    }

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

    public void refreshAll(){
        for (int i =0; i<bins.size();i++){
            afficheur.refreshBin(i);
        }
    }

}


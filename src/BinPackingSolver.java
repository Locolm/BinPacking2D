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
        selectAndSwitch();
        //switchAndRefresh(test1,test2);
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

    public void switchAndRefresh(List<Integer> pos1,List<Integer> pos2,int wait) throws Exception {
        //pos1 = position OU item, pos2 = item
        if (pos1.isEmpty() || pos2.isEmpty()){
            throw new Exception("On ne peut pas échanger des positions vides");
        }
        if (pos1==pos2){
            throw new Exception("On ne peut pas échanger les mêmes positions");
        }
        List<Integer> copy1 = new ArrayList<>(pos1);
        copy1.removeLast();
        Bin bin1 = getSousBinWithPos(copy1);
        List<Item> items = new ArrayList<>();
        if (!bin1.sousBins.isEmpty() && bin1.sousBins.get(pos1.getLast()).item!=null) {
            //if item not null pos1.getLast == 0
            items.add(bin1.sousBins.get(pos1.getLast()).item);
            bin1.sousBins.get(pos1.getLast()).item = null;
            if (bin1.sousBins.get(1).sousBins.isEmpty() && bin1.sousBins.get(2).sousBins.isEmpty()){
                // si les 2 sont vides
                bin1.sousBins = new ArrayList<>();
            } else if(bin1.sousBins.get(1).sousBins.isEmpty() && !bin1.sousBins.get(2).sousBins.isEmpty()){
                //si juste le premier est vide, on fusionne en réduisant le 2ème sousBin à 0 0
                bin1.sousBins.get(0).width += bin1.sousBins.get(1).width;
                bin1.sousBins.get(0).height += bin1.sousBins.get(1).height;
                bin1.sousBins.get(1).width=0;
                bin1.sousBins.get(1).height=0;
            }
        }
        List<Integer> copy2 = new ArrayList<>(pos2);
        copy2.removeLast();
        Bin bin2 = getSousBinWithPos(copy2);
        if (!bin2.sousBins.isEmpty() && bin2.sousBins.get(pos2.getLast()).item!=null) {
            //if item not null pos1.getLast == 0
            if (pos1.getFirst()>pos2.getFirst()){items.add(bin2.sousBins.get(pos2.getLast()).item);}
            else {items.addFirst(bin2.sousBins.get(pos2.getLast()).item);}

            bin2.sousBins.get(pos2.getLast()).item = null;
            if (bin2.sousBins.get(1).sousBins.isEmpty() && bin2.sousBins.get(2).sousBins.isEmpty()){
                // si les 2 sont vides
                bin2.sousBins = new ArrayList<>();
            } else if(bin2.sousBins.get(1).sousBins.isEmpty() && !bin2.sousBins.get(2).sousBins.isEmpty()){
                //si juste le premier est vide, on fusionne en réduisant le 2ème sousBin à 0 0
                bin2.sousBins.get(0).width += bin2.sousBins.get(1).width;
                bin2.sousBins.get(0).height += bin2.sousBins.get(1).height;
                bin2.sousBins.get(1).width=0;
                bin2.sousBins.get(1).height=0;
            }
        }
        refreshAll(wait);
        placeItem(items);
        refreshAll(wait);
    }

    public static List<Item> mergeLists(List<Item> list1, List<Item> list2) {
        List<Item> mergedList = new ArrayList<>();

        // Add the first two elements of each list to the merged list
        for (int i = 0; i < 2; i++) {
            if (i < list1.size()) {
                mergedList.add(list1.get(i));
            }
            if (i < list2.size()) {
                mergedList.add(list2.get(i));
            }
        }

        // Add the remaining elements of list1
        for (int i = 2; i < list1.size(); i++) {
            mergedList.add(list1.get(i));
        }

        // Add the remaining elements of list2
        for (int i = 2; i < list2.size(); i++) {
            mergedList.add(list2.get(i));
        }

        return mergedList;
    }

/*    public void swapSousBins(int index1, int index2, Bin bin) {
        if (index1 < 0 || index1 >= bin.sousBins.size() || index2 < 0 || index2 >= bin.sousBins.size()) {
            throw new IndexOutOfBoundsException("Les indices sont hors limites");
        }
        List<Integer> tempPosItem = new ArrayList<>(bin.sousBins.get(index1).item.position);
        tempPosItem.set(tempPosItem.size()-1,index1);
        //changer la valeur de pos pour tous les sousbins enfants et pour les items
        bin.sousBins.get(index1).item.setPosition(tempPosItem);
        Bin temp = bin.sousBins.get(index1);
        bin.sousBins.set(index1, bin.sousBins.get(index2));
        bin.sousBins.set(index2, temp);
    }*/

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

    public void selectAndSwitch() throws Exception {
        for (int i = 0; i < bins.size(); i++) {
            for (int j = i + 1; j < bins.size() - 1; j++) {
                if(!Objects.equals(items.get(i).position.getFirst(), items.get(j).position.getFirst())) {
                    switchAndRefresh(items.get(i).position,items.get(j).position, 200);
                }
            }
        }
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


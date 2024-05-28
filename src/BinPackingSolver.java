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

    public void solveRecuit(){
        for (int i = 0; i < items.size()-1; i++)
        {
            for (int j = i+1; j < items.size(); j++)
            {
                //récupérer les ids des items dans les mêmes sousBins des items i et j qui sont ceux qui vont être inversés
                //on vide les sousBins correspondants
                List<Integer> posI = items.get(i).position;
                List<Integer> remainsPosI = posI.subList(1, posI.size());
                List<Integer> idsItemsI = bins.get(posI.getFirst()).getIdsItemsInSousBins(bins.get(posI.getFirst()).sousBins);
                bins.get(posI.getFirst()).setSousBins(null,remainsPosI);

                List<Integer> posJ = items.get(j).position;
                List<Integer> remainsPosJ = posJ.subList(1, posJ.size());
                List<Integer> idsItemsJ = bins.get(posJ.getFirst()).getIdsItemsInSousBins(bins.get(posJ.getFirst()).sousBins);
                bins.get(posJ.getFirst()).setSousBins(null,remainsPosJ);

                //on replace ensuite les items dans l'ordre (en inversant I et J)
                // on utilise la fonction de remplissage automatique pour placer les items, dans un bin, s'il n'y a plus de place
                //on crée un nouveau bin
            }
        }
    }

    public void display() {
        afficheur = new Afficheur(bins);
        afficheur.display();
    }

}

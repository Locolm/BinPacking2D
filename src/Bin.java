import java.util.ArrayList;
import java.util.List;

public class Bin {
    int width;
    int height;
    Item item;

    List<Integer> position;

    List<Bin> sousBins;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Item getItem() {
        return item;
    }

    public List<Bin> getSousBins() {
        return sousBins;
    }

    public Bin(int width, int height) {
        this.width = width;
        this.height = height;
        this.item = null;
        this.sousBins = new ArrayList<>();
    }

    public boolean addItem(Item _item, List<Integer> pos) {
        return fits(_item, pos, 2);
    }

    public boolean addItemWithRotation(Item _item, List<Integer> pos) {
        _item.rotate();
        return fits(_item, pos, 2);
    }

    private void divideBin(Item _item, List<Integer> pos) {
        Bin sousBinTL = new Bin(_item.width, _item.height);
        sousBinTL.item = _item;
        sousBinTL.item.setPosition(pos);
        sousBins.add(sousBinTL);
        Bin sousBinTR = new Bin(this.width - _item.width ,_item.height );
        Bin sousBinB = new Bin(this.width, this.height - _item.height);
        sousBins.add(sousBinTR);
        sousBins.add(sousBinB);
    }

    private boolean fits(Item _item, List<Integer> pos, int index) {
        //si pos pas de taille suffisante (par rapport à index), on ajoute des 0
        if(pos.size() < index+1){
            pos.add(0);
        }
        //si pos trop grand réduire la taille de la liste (par rapport à index), on supprime le dernier élément
        if(pos.size() > index+1){
            pos.removeLast();
        }
        //création des sous-bins et placement de l'item
        if (sousBins.isEmpty()) {
            if (_item.width <= this.width && _item.height <= this.height) {
                divideBin(_item, pos);
                return true;
            }
        } else {
            for (Bin sousBin : sousBins) {
                if (sousBin.item == null && sousBin.fits(_item, pos, index+1)) {
                    return true;
                }
                pos.set(index, pos.get(index) + 1);
            }
        }
        return false;
    }
}
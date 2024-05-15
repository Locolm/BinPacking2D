import java.util.ArrayList;
import java.util.List;

public class Bin {
    int width;
    int height;
    Item item;

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
        return fits(_item, new ArrayList<>(pos));
    }

    public boolean addItemWithRotation(Item _item, List<Integer> pos) {
        _item.rotate();
        return fits(_item, new ArrayList<>(pos));
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

    private boolean fits(Item _item, List<Integer> pos) {
        pos.add(0);

        //création des sous-bins et placement de l'item
        if (sousBins.isEmpty()) {
            if (_item.width <= this.width && _item.height <= this.height) {
                divideBin(_item, pos);
                return true;
            }
        } else {
            for (Bin sousBin : sousBins) {
                if (sousBin.item == null && sousBin.fits(_item, pos)) {
                    return true;
                }
                //pos.removeLast();
                pos.set(pos.size()-1,pos.getLast() + 1);
            }
        }
        return false;
    }
}
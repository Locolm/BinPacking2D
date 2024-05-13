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

    public boolean addItem(Item _item) {
        return fits(_item);
    }

    public boolean addItemWithRotation(Item _item) {
        _item.rotate();
        return fits(_item);
    }

    private void divideBin(Item _item) {
        Bin sousBinTL = new Bin(_item.width, _item.height);
        sousBinTL.item = _item;
        sousBins.add(sousBinTL);
        Bin sousBinTR = new Bin(this.width - _item.width ,_item.height );
        Bin sousBinB = new Bin(this.width, this.height - _item.height);
        sousBins.add(sousBinTR);
        sousBins.add(sousBinB);
    }

    private boolean fits(Item _item) {
        if (sousBins.isEmpty()) {
            if (_item.width <= this.width && _item.height <= this.height) {
                divideBin(_item);
                return true;
            }
        } else {
            for (Bin sousBin : sousBins) {
                if (sousBin.item == null && sousBin.fits(_item)) {
                    return true;
                }
            }
        }
        return false;
    }
}
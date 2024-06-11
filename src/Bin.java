import java.util.ArrayList;
import java.util.List;

public class Bin {
    int width;
    int height;
    Item item;

    List<Bin> sousBins;

    List<Integer> position;

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

    public Bin(int width, int height, List<Integer> position) {
        this.width = width;
        this.height = height;
        this.item = null;
        this.position = position;
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
        List<Integer> posTL = new ArrayList<>(position);
        posTL.add(0);
        Bin sousBinTL = new Bin(_item.width, _item.height,posTL);
        sousBinTL.item = _item;
        sousBinTL.item.setPosition(posTL);
        sousBins.add(sousBinTL);
        List<Integer> posTR = new ArrayList<>(position);
        posTR.add(sousBins.size());
        Bin sousBinTR = new Bin(this.width - _item.width ,_item.height,posTR);
        sousBins.add(sousBinTR);
        List<Integer> posB = new ArrayList<>(position);
        posB.add(sousBins.size());
        Bin sousBinB = new Bin(this.width, this.height - _item.height,posB);
        sousBins.add(sousBinB);
    }

    private boolean fits(Item _item) {
        //création des sous-bins et placement de l'item
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

    public Bin getSousBins(List<Bin> sousBins, List<Integer> pos) {
        int index = pos.getFirst();
        if (pos.size() == 1) {
            return sousBins.get(index);
        } else {
            Bin current = sousBins.get(index);
            List<Integer> remainingPos = pos.subList(1, pos.size());
            return getSousBins(current.sousBins, remainingPos);
        }
    }

    public List<Item> getItemsInSousBins(List<Bin> sousBins) {
        List<Item> items = new ArrayList<>();
        for (Bin sousBin : sousBins) {
            if (sousBin.item != null) {
                items.add(sousBin.item);
            }
            items.addAll(getItemsInSousBins(sousBin.sousBins));
        }
        return items;
    }


    public void setSousBins(Bin sousBin, List<Integer> pos) {
        int index = pos.getFirst();
        if (pos.size() == 1) {
            this.sousBins.set(index, sousBin);
        } else {
            List<Integer> remainingPos = pos.subList(1, pos.size());
            Bin child = this.sousBins.get(index);
            child.setSousBins(sousBin, remainingPos);
        }
    }
}
import java.util.ArrayList;
import java.util.List;

public class Bin {
    int width;
    int height;
    Item item;

    List<Bin> sousBins;

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
        Bin sousBinBL = new Bin(_item.width, this.height - _item.height);
        Bin sousBinBR = new Bin(this.width - _item.width, this.height - _item.height);
        sousBins.add(sousBinTR);
        sousBins.add(sousBinBL);
        sousBins.add(sousBinBR);
    }

    private void fusionBinsTRBR(Item _item) {
        int wTemp1 = sousBins.get(1).width;
        int hTemp1 = sousBins.get(1).height;
        int wTemp2 = sousBins.get(2).width;
        int hTemp2 = sousBins.get(2).height;
        sousBins.remove(3);
        sousBins.remove(2);
        sousBins.remove(1);
        //fusion
        Bin sousBin = new Bin(wTemp1, hTemp1 + hTemp2);
        sousBin.item = _item;
        sousBins.add(sousBin);
        sousBins.add(new Bin(wTemp2, hTemp2)); //BL
    }

    private void fusionBinsBLBR(Item _item) {
        int wTemp1 = sousBins.get(1).width;
        int hTemp1 = sousBins.get(1).height;
        int wTemp2 = sousBins.get(2).width;
        int hTemp2 = sousBins.get(2).height;
        sousBins.remove(3);
        sousBins.remove(2);
        sousBins.remove(1);
        sousBins.add(new Bin(wTemp1, hTemp1)); //TR
        //fusion
        Bin sousBin = new Bin(wTemp2 + wTemp1, hTemp2);
        sousBin.item = _item;
        sousBins.add(sousBin);

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

    //A appeller après une rotation pck sinon il l'ajoute dans le bin même si il aurait pu rentrer dans un sous bins avec une rotation
    private boolean fitsFusion(Item _item){
        //si il y a bien 4 éléments dans la liste sousBins (TL, TR, BL, BR) => pas eu de fusions
        if (sousBins.size() == 4 && sousBins.get(3).item == null) {
            if (sousBins.get(1).item == null && _item.width <= sousBins.get(1).width && _item.height <= sousBins.get(1).height + sousBins.get(3).height) {
                fusionBinsTRBR(_item);
                return true;
            } else if (sousBins.get(2).item == null && _item.width <= sousBins.get(2).width + sousBins.get(3).width && _item.height <= sousBins.get(2).height) {
                fusionBinsBLBR(_item);
                return true;
            }
        }
        return false;
    }
}
import java.util.ArrayList;
import java.util.List;

public class BinProcessor {
    private static int currentCount = 0;

    public static Result findNthElement(List<Bin> bins, int n) {
        List<Integer> pos = new ArrayList<>();
        return findNthElementHelper(bins, pos, n);
    }

    private static Result findNthElementHelper(List<Bin> bins, List<Integer> pos, int n) {
        for (int i = 0; i < bins.size(); i++) {
            pos.add(i);
            Bin bin = bins.get(i);

            if (bin.item != null) {
                currentCount++;
                if (currentCount == n) {
                    return new Result(true, new ArrayList<>(pos));
                }
            } else if (checkCriteria(bin)) {
                currentCount++;
                if (currentCount == n) {
                    return new Result(false, new ArrayList<>(pos));
                }
            }

            if (bin.sousBins != null && !bin.sousBins.isEmpty()) {
                Result found = findNthElementHelper(bin.sousBins, pos, n);
                if (found != null) {
                    return found;
                }
            }

            pos.removeLast();
        }
        return null;
    }

    public static ResultBin removeBinAtPosition(Bin rootBin, List<Integer> pos) {
        if (pos == null || pos.isEmpty()) {
            throw new IllegalArgumentException("La position ne peut pas être vide");
        }

        Bin parentBin = rootBin;
        Bin targetBin = null;

        // Trouver le sous-bin cible et son parent
        for (int i = 0; i < pos.size() - 1; i++) {
            int index = pos.get(i);
            if (index >= parentBin.sousBins.size()) {
                throw new IndexOutOfBoundsException("Position invalide : " + pos);
            }
            parentBin = parentBin.sousBins.get(index);
        }

        int targetIndex = pos.getLast();
        if (targetIndex >= parentBin.sousBins.size()) {
            throw new IndexOutOfBoundsException("Position invalide : " + pos);
        }
        targetBin = parentBin.sousBins.get(targetIndex);

        // Récupérer les items du sous-bin cible
        List<Bin> singleBinTarget = new ArrayList<>();
        singleBinTarget.add(targetBin);
        List<Item> items = targetBin.getItemsInSousBins(singleBinTarget);

        // Supprimer le sous-bin cible
        parentBin.sousBins.remove(targetIndex);

        return new ResultBin(items, rootBin);
    }

    private static boolean checkCriteria(Bin bin) {
        return bin.item == null && (bin.sousBins == null || bin.sousBins.isEmpty()) && (bin.width == 0 || bin.height == 0);
    }
}

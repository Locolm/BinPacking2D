import java.util.List;

// Classe de résultat pour encapsuler le booléen et la position
public class ResultBin {
    public List<Item> items;
    public Bin bin;

    public ResultBin(List<Item> items, Bin bin) {
        this.items = items;
        this.bin = bin;
    }

    @Override
    public String toString() {
        return "ResultBin{items=" + items + ", bin=" + bin + '}';
    }
}
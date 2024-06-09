import java.util.ArrayList;
import java.util.List;

// Classe de résultat pour encapsuler le booléen et la position
public class Result {
    public boolean isItem;
    public List<Integer> position;

    public Result(boolean isItem, List<Integer> position) {
        this.isItem = isItem;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Result{isItem=" + isItem + ", position=" + position + '}';
    }
}
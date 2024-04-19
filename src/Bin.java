import java.util.ArrayList;
import java.util.List;

public class Bin {
    int width;
    int height;
    List<Item> items;

    public Bin(int width, int height) {
        this.width = width;
        this.height = height;
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (fits(item)) {
            items.add(item);
            return true;
        }
        return false;
    }

    private boolean fits(Item item) {
        for (Item existingItem : items) {
            if (existingItem.x + item.width <= width && existingItem.y + item.height <= height) {
                return true;
            }
            if (existingItem.x + item.height <= height && existingItem.y + item.width <= width) {
                return true;
            }
        }
        return false;
    }
}
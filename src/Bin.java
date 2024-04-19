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

    public boolean removeItem(Item item) {
        return items.remove(item);
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

    public boolean addItemWithRotation(Item item) {
        if (fits(item)) {
            items.add(item);
            return true;
        }
        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item {
    int id;
    int width;
    int height;
    Color color;
    List<Integer> position;

    public Item(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.color = this.generateRandomColor();
        position = new ArrayList<>();
    }

    private Color generateRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(205), random.nextInt(205), random.nextInt(205));
    }

    public Color getColor(){
        return this.color;
    }

    public void rotate(){
        int temp = width;
        width = height;
        height = temp;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(List<Integer> pos){
        this.position= pos;
    }

}

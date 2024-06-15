package BPP2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item {
    public int id;
    public int width;
    public int height;
    public Color color;
    public List<Integer> position;

    public Item(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.color = this.generateRandomColor();
        position = new ArrayList<>();
    }

    private Color generateRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(190)+30, random.nextInt(190)+30, random.nextInt(190)+40);
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

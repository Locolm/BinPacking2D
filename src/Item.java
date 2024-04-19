public class Item {
    int id;
    int width;
    int height;
    int x;
    int y;

    public Item(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.x = -1;
        this.y = -1;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

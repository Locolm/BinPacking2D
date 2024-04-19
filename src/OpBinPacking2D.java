import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OpBinPacking2D {

    public boolean placeItem(Bin bin, Item item) {
        int binWidth = bin.width;
        int binHeight = bin.height;

        for (int i = 0; i <= binHeight - item.height; i++) {
            for (int j = 0; j <= binWidth - item.width; j++) {
                item.x = j;
                item.y = i;
                if (bin.addItem(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void drawBin(Bin bin) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(bin.width, bin.height);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Item item : bin.items) {
                    g.setColor(Color.BLUE);
                    g.fillRect(item.x, item.y, item.width, item.height);
                    g.setColor(Color.BLACK);
                    g.drawRect(item.x, item.y, item.width, item.height);
                    g.drawString(Integer.toString(item.id), item.x + 5, item.y + 15);
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }
}

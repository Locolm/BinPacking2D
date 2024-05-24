import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Lecteur {
    static int width;
    static int height;

    static List<Item> items = new ArrayList<>();

    public static void readFileBP2(String filePath) {

        try {
            BufferedReader lecteur = new BufferedReader(new FileReader(filePath));
            String ligne;

            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.startsWith("BIN_WIDTH:")) {
                    width = Integer.parseInt(ligne.split(":")[1].trim());
                } else if (ligne.startsWith("BIN_HEIGHT:")) {
                    height = Integer.parseInt(ligne.split(":")[1].trim());
                } else if (ligne.startsWith("ITEMS")) {
                    while ((ligne = lecteur.readLine()) != null && !ligne.isEmpty()) {
                        String[] infosItem = ligne.trim().split("\\s+");
                        int id = Integer.parseInt(infosItem[0]);
                        int width = Integer.parseInt(infosItem[1]);
                        int height = Integer.parseInt(infosItem[2]);
                        items.add(new Item(id, width, height));
                    }
                }
            }
            lecteur.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Mélange des items
        //Collections.shuffle(items);
        //Trie des items par ordre décroissant de leur surface (width x height)
        items.sort((item1, item2) -> item2.width * item2.height - item1.width * item1.height);

        // Affichage des valeurs lues
        System.out.println("Dimensions du bin : " + width + " x " + height);
        System.out.println("Items :");
        for (Item item : items) {
            System.out.println("ID : " + item.id + ", Width : " + item.width + ", Height : " + item.height);
        }
    }
}

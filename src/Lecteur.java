import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lecteur {
    static Bin bin = new Bin(0, 0);;

    static List<Item> items = new ArrayList<>();;

    public static void lireFichierBP2(String cheminFichier) {

        try {
            BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier));
            String ligne;

            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.startsWith("BIN_WIDTH:")) {
                    bin.width = Integer.parseInt(ligne.split(":")[1].trim());
                } else if (ligne.startsWith("BIN_HEIGHT:")) {
                    bin.height = Integer.parseInt(ligne.split(":")[1].trim());
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

        // Affichage des valeurs lues
        System.out.println("Dimensions du bin : " + bin.width + " x " + bin.height);
        System.out.println("Items :");
        for (Item item : items) {
            System.out.println("ID : " + item.id + ", Width : " + item.width + ", Height : " + item.height);
        }
    }
}

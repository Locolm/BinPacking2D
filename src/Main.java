public class Main {

    public static void main(String[] args) {
        Lecteur lecteur =new Lecteur();
        Lecteur.lireFichierBP2("Ressources/binPacking2d-01.bp2d");
        OpBinPacking2D opBinPacking2D = new OpBinPacking2D();

        for (Item item : Lecteur.items) {
            if (!opBinPacking2D.placeItem(Lecteur.bin, item)) {
                System.out.println("Item " + item.getId() + " ne rentre pas dans le bin.");
            }
        }

        opBinPacking2D.drawBin(Lecteur.bin);
    }

}

public class Main {

    public static void main(String[] args) {
        Lecteur.readFileBP2("Ressources/binPacking2d-02.bp2d");
        BinPackingSolver binPackingSolver = new BinPackingSolver(Lecteur.width, Lecteur.height, Lecteur.items);
        binPackingSolver.init();

        System.out.println("hello");
    }

}

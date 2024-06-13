public class Main {

    public static void main(String[] args) throws Exception {
        Lecteur.readFileBP2("Ressources/binPacking2d-03.bp2d");
        BinPackingSolver binPackingSolver = new BinPackingSolver(Lecteur.width, Lecteur.height, Lecteur.items);
        binPackingSolver.init();

        System.out.println("Process ended with success");
    }

}

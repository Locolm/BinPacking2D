public class Main {

    public static void main(String[] args) {
        Lecteur.lireFichierBP2("Ressources/binPacking2d-01.bp2d");
        BinPackingSolver binPackingSolver = new BinPackingSolver(Lecteur.width, Lecteur.height, Lecteur.items);
        binPackingSolver.solve();
    }

}

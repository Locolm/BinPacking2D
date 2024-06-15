import App.Lecteur;
import BPP2D.BinPackingSolver;
import Metaheuristics.SimulatedAnnealing;
import Metaheuristics.Tabu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        boolean entryValid = false;
        Scanner scanner = new Scanner(System.in);
        while (!entryValid) {
            System.out.println("Select file to run tabou on {1 - 13}");

            // Lire l'entrée utilisateur
            int fileNumber = scanner.nextInt();

            // Vérifier si le numéro de fichier est valide
            if (fileNumber < 1 || fileNumber > 13) {
                System.out.println("Invalid file number. Please select a number between 1 and 13.");
            } else {
                entryValid = true;
                // Construire le nom de fichier en fonction de l'entrée utilisateur
                String fileName = String.format("Ressources/binPacking2d-%02d.bp2d", fileNumber);
                System.out.println("Running tabou on file: " + fileName);

                // Appeler la méthode App.Lecteur.readFileBP2 avec le nom de fichier sélectionné
                Lecteur.readFileBP2(fileName);
            }
        }

        // Ask metaheuristic to run
        entryValid = false;
        while (!entryValid) {
            System.out.println("Select metaheuristic to run {1 (Tabu) - 2 (Simulated Annealing)}");

            // Lire l'entrée utilisateur
            int metaheuristicNumber = scanner.nextInt();

            // Vérifier si le numéro de métaheuristique est valide
            if (metaheuristicNumber < 1 || metaheuristicNumber > 2) {
                System.out.println("Invalid metaheuristic number. Please select a number between 1 and 2.");
            } else {
                entryValid = true;
                // Appeler la méthode pour exécuter la métaheuristique sélectionnée
                if (metaheuristicNumber == 1) {
                    binPresetTabu(scanner);
                } else if (metaheuristicNumber == 2){
                    binPresetSimulatedAnnealing(scanner);
                }
            }
        }


        binPresetTabu(scanner);

        System.out.println("Process ended with success");
    }

    private static void binPresetTabu(Scanner scanner) throws Exception {
        boolean entryValid;
        entryValid = false;
        boolean alreadyShowed = false;
        while (!entryValid) {
            if (alreadyShowed){System.out.println("Do you want to run preset Tabou?\n Y/N");}
            String input = scanner.nextLine().trim().toLowerCase();

            // Vérifier si l'utilisateur a entré une réponse valide
            if (input.isEmpty()){
                alreadyShowed = true;
            }
            if (!input.isEmpty()){
                if (input.equals("y") || input.equals("yes") || input.equals("1")) {
                    entryValid = true;
                    // Appeler la méthode pour exécuter le Tabou prédéfini
                    BinPackingSolver binPackingSolver = new BinPackingSolver(Lecteur.width, Lecteur.height, Lecteur.items, new Tabu(15,10,50,true));
                    binPackingSolver.init();
                } else if (input.equals("n") || input.equals("no") || input.equals("0")) {
                    entryValid = true;
                    binMainTabu(scanner);
                } else {
                    System.out.println("Invalid input. Please enter y or n");
                }
            }
        }
    }

    public static void binPresetSimulatedAnnealing(Scanner scanner) throws Exception {
        boolean entryValid;
        entryValid = false;
        boolean alreadyShowed = false;
        while (!entryValid) {
            if (alreadyShowed){System.out.println("Do you want to run preset Simulated Annealing?\n Y/N");}
            String input = scanner.nextLine().trim().toLowerCase();

            // Vérifier si l'utilisateur a entré une réponse valide
            if (input.isEmpty()){
                alreadyShowed = true;
            }
            if (!input.isEmpty()){
                if (input.equals("y") || input.equals("yes") || input.equals("1")) {
                    entryValid = true;
                    // Appeler la méthode pour exécuter le Tabou prédéfini
                    BinPackingSolver binPackingSolver = new BinPackingSolver(Lecteur.width, Lecteur.height, Lecteur.items, new SimulatedAnnealing(15,100,0.99,0, true));
                    binPackingSolver.init();
                } else if (input.equals("n") || input.equals("no") || input.equals("0")) {
                    entryValid = true;
                    binMainSimulatedAnnealing(scanner);
                } else {
                    System.out.println("Invalid input. Please enter y or n");
                }
            }
        }
    }

    private static void binMainSimulatedAnnealing(Scanner scanner) throws Exception {

        int iterations = 0;
        int initialTemperature = 0;
        int coolingRate = 0;
        int waitingTime = 0;
        boolean displayNeighbour = false;

        // Demander à l'utilisateur de sélectionner les valeurs
        while (true) {
            System.out.println("Select Simulated Annealing iterations (must be positive): ");
            iterations = scanner.nextInt();
            if (iterations > 0) {
                break;
            } else {
                System.out.println("Iterations must be positive. Please try again.");
            }
        }

        while (true) {
            System.out.println("Select Simulated Annealing initial temperature (must be positive): ");
            initialTemperature = scanner.nextInt();
            if (initialTemperature > 0) {
                break;
            } else {
                System.out.println("Initial temperature must be positive. Please try again.");
            }
        }

        while (true) {
            System.out.println("Select Simulated Annealing cooling rate (must be positive): ");
            coolingRate = scanner.nextInt();
            if (coolingRate > 0) {
                break;
            } else {
                System.out.println("Cooling rate must be positive. Please try again.");
            }
        }

        while (true) {
            System.out.println("Select Waiting Time between each iteration (0 for no delay): ");
            waitingTime = scanner.nextInt();
            if (waitingTime >= 0) {
                break;
            } else {
                System.out.println("Waiting Time must be positive. Please try again.");
            }
        }

        while (true) {
            System.out.println("Do you want to see the selection of neighbours between each iteration? (Y/N): ");
            String displayNeighbourInput = scanner.next().trim().toLowerCase();
            if (displayNeighbourInput.equals("y") || displayNeighbourInput.equals("yes")) {
                displayNeighbour = true;
                break;
            } else if (displayNeighbourInput.equals("n") || displayNeighbourInput.equals("no")) {
                displayNeighbour = false;
                break;
            } else {
                System.out.println("Invalid input. Please enter Y/y/yes or N/n/no.");
            }
        }

        scanner.close();

        // Créer l'instance de BinPackingSolver avec les valeurs choisies
        BinPackingSolver binPackingSolver = new BinPackingSolver(Lecteur.width, Lecteur.height, Lecteur.items, new SimulatedAnnealing(iterations, initialTemperature, coolingRate, waitingTime, displayNeighbour));
        binPackingSolver.init();
    }

    private static void binMainTabu(Scanner scanner) throws Exception {

        int iterations = 0;
        int lengthTabou = 0;
        int waitingTime = 0;
        boolean displayNeighbour = false;

        // Demander à l'utilisateur de sélectionner les valeurs
        while (true) {
            System.out.println("Select Tabou iterations (must be positive): ");
            iterations = scanner.nextInt();
            if (iterations > 0) {
                break;
            } else {
                System.out.println("Iterations must be positive. Please try again.");
            }
        }

        while (true) {
            System.out.println("Select Tabou length (must be positive): ");
            lengthTabou = scanner.nextInt();
            if (lengthTabou > 0) {
                break;
            } else {
                System.out.println("Tabou length must be positive. Please try again.");
            }
        }

        while (true) {
            System.out.println("Select Waiting Time between each iteration (0 for no delay): ");
            waitingTime = scanner.nextInt();
            if (waitingTime >= 0) {
                break;
            } else {
                System.out.println("Waiting Time must be positive. Please try again.");
            }
        }

        while (true) {
            System.out.println("Do you want to see the selection of neighbours between each iteration? (Y/N): ");
            String displayNeighbourInput = scanner.next().trim().toLowerCase();
            if (displayNeighbourInput.equals("y") || displayNeighbourInput.equals("yes")) {
                displayNeighbour = true;
                break;
            } else if (displayNeighbourInput.equals("n") || displayNeighbourInput.equals("no")) {
                displayNeighbour = false;
                break;
            } else {
                System.out.println("Invalid input. Please enter Y/y/yes or N/n/no.");
            }
        }

        scanner.close();

        // Créer l'instance de BinPackingSolver avec les valeurs choisies
        BinPackingSolver binPackingSolver = new BinPackingSolver(Lecteur.width, Lecteur.height, Lecteur.items, new Tabu(iterations, lengthTabou, waitingTime, displayNeighbour));
        binPackingSolver.init();
    }
}



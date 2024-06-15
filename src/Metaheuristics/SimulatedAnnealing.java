package Metaheuristics;

import BPP2D.Bin;
import BPP2D.BinPackingSolver;

import java.util.ArrayList;

import static App.Lecteur.items;

public class SimulatedAnnealing implements Metaheuristic {

    public int iteration;
    public double initialTemperature;
    public double coolingRate;
    public int waitingTime;
    public boolean displayNeighbour;

    public SimulatedAnnealing(int iteration, double initialTemperature, double coolingRate, int waitingTime, boolean displayNeighbour) {
        this.iteration = iteration;
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
        this.waitingTime = waitingTime;
        this.displayNeighbour = displayNeighbour;
    }

    @Override
    public void run(BinPackingSolver solver) {
        System.out.println("Running Simulated Annealing with { iteration:" + iteration + ", initialTemperature:" + initialTemperature + ", coolingRate:" + coolingRate + ", waitingTime:" + waitingTime + ", displayNeighbour:" + displayNeighbour + "}");

        // Best solution and eval
        int bestEval = solver.bestEval;
        ArrayList<Bin> bestSolution = new ArrayList<>(solver.bins);

        //parcours des voisins et selection du meilleur dans this.bins
        for (int i = 0; i < iteration; i++) {
            solver.originalSolution = new ArrayList<>(solver.bins);
            try {
                solver.selectAndSwitchRandom();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            solver.bins = new ArrayList<>();
            solver.placeItem(items);
            if (displayNeighbour) {
                solver.refreshAll(waitingTime);
            }
            int newEval = solver.eval(solver.bins);
            if (newEval < bestEval) {
                bestEval = newEval;
                bestSolution = new ArrayList<>(solver.bins);
            } else {
                double acceptanceProbability = Math.exp((double) (bestEval - newEval) / initialTemperature);
                if (acceptanceProbability > Math.random()) {
                    bestEval = newEval;
                    bestSolution = new ArrayList<>(solver.bins);
                }
            }
            initialTemperature *= 1 - coolingRate;
        }
        solver.bins = new ArrayList<>(bestSolution);
        solver.displayBestSolution(0);
        System.out.println("Simulated Annealing ended");
        System.out.println("Best solution found : " + solver.bestSolution.size() + " bins");
    }
}

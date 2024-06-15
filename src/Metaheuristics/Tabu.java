package Metaheuristics;

import BPP2D.BinPackingSolver;

import java.util.ArrayList;

import static App.Lecteur.items;

public class Tabu implements Metaheuristic {

    //asked user
    int lengthTabou;
    int iteration ;
    boolean displayNeighbour ;
    int waitingTime;


    public Tabu(int iteration,int lengthTabou, int waitingTime,boolean displayNeighbour){
        this.iteration = iteration;
        this.lengthTabou = lengthTabou;
        this.waitingTime = waitingTime;
        this.displayNeighbour = displayNeighbour;
    }

    public Tabu(){
        this.iteration = 100;
        this.lengthTabou = 10;
        this.waitingTime = 100;
        this.displayNeighbour = false;
    }

    @Override
    public void run(BinPackingSolver solver){
        System.out.println("Running Tabou with { iteration:"+iteration+", lengthTabou:"+lengthTabou+", waitingTime:"+waitingTime+", displayNeighbour:"+displayNeighbour+"}");

        //parcours des voisins et selection du meilleur dans this.bins
        for(int i=0;i<iteration;i++) {
            solver.originalSolution = new ArrayList<>(solver.bins);
            try {
                solver.selectAndSwitch(waitingTime, displayNeighbour, lengthTabou);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            solver.bins = new ArrayList<>();
            solver.placeItem(items);
            System.out.println(i);
            if (displayNeighbour) {
                solver.refreshAll(waitingTime);
            }
        }
        solver.refreshAll(0);
    }
}

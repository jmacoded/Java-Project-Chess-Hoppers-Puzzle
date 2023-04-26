package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    private ArrayList<Configuration> solution;
    private String savedFileName;
    private Character frog;
    private char[][] grid;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    public HoppersModel(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        this.solution = null;
        this.savedFileName = filename;
        this.frog = null;
        this.grid = null;
    }

    public Character getFrog(int row, int col) {
        return '0';
    }

    public void moveFrog(int row, int col) {

    }

    public void hint() {
        Solver solver = new Solver(this.currentConfig);
        solution = solver.getShortestlist();

    }
}

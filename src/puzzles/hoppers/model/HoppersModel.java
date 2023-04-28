package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    private Character frog;
    private char[][] grid;
    private int savedRow;
    private int savedCol;

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
        this.grid = currentConfig.getGrid();
        this.frog = null;
        this.savedRow = -1;
        this.savedCol = -1;
    }

    public boolean hint() {
        Solver solver = new Solver(this.currentConfig);
        ArrayList<Configuration> solverShortestList = solver.getShortestlist();
        if (solverShortestList.size() == 0) {
            return false;
        } else {
            this.currentConfig = (HoppersConfig) solverShortestList.get(1);
            this.grid = this.currentConfig.getGrid();
            return true;
        }
    }

    public HoppersModel load(String filename) throws IOException {
        HoppersModel hoppersModel = new HoppersModel(filename);
        String[] file = filename.split("/");
        System.out.println("Loaded: " + file[2]);
        System.out.println(hoppersModel);
        return hoppersModel;
    }

    public int select(int row, int col) {
        if (this.frog == null) {
            if (this.grid[row][col] == 'G' || this.grid[row][col] == 'R') {
                this.frog = this.grid[row][col];
                this.savedRow = row;
                this.savedCol = col;
                return 1;
            } else {
                return 2;
            }
        } else {
            // check if the desired location is empty
            if (this.grid[row][col] != '.') {
                this.frog = null;
                return 4;
            }
            // receives and verifies the row and col for the midpoint between two points
            int middleRow = -1;
            int middleCol = -1;
            if (row < this.savedRow) {
                middleRow = this.savedRow - 1;
            } else if (row > this.savedRow) {
                middleRow = row - 1;
            }
            if (col < this.savedCol) {
                middleCol = this.savedCol - 1;
            } else if (col > this.savedCol) {
                middleCol = col - 1;
            }
            if (middleRow == -1 || middleCol == -1) {
                this.frog = null;
                return 4;
            }
            // proceeds to create a test config and compare it to currentConfig's neighbors
            Collection<Configuration> neighbors = this.currentConfig.getNeighbors();
            HoppersConfig testHoppersConfig = new HoppersConfig(this.currentConfig);
            char[][] testGrid = testHoppersConfig.getGrid();
            testGrid[savedRow][savedCol] = '.';
            testGrid[row][col] = this.frog;
            testGrid[middleRow][middleCol] = '.';
            if (neighbors.contains(testHoppersConfig)) {
                this.currentConfig = testHoppersConfig;
                this.grid = this.currentConfig.getGrid();
                this.frog = null;
                return 3;
            } else {
                this.frog = null;
                return 4;
            }
        }
    }

    public int getSavedRow() {
        return this.savedRow;
    }

    public int getSavedCol() {
        return this.savedCol;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        boolean firstTime = false;
        string.append("   ");
        for (int c= 0; c < (this.currentConfig.getColumnDIM()); c++){
            if (firstTime) {
                string.append(" " + c);
            } else {
                string.append(c);
                firstTime = true;
            }
        }
        firstTime = false;
        string.append("\n");
        string.append("  ");
        for (int c = 2; c < (2 + 2 * this.currentConfig.getColumnDIM()); c++){
            string.append("-");
        }
        string.append("\n");
        for (int c = 0; c < this.currentConfig.getRowDIM(); c++) {
            string.append(c + "| ");
            for (int r = 0; r < this.currentConfig.getColumnDIM(); r++) {
                if (firstTime) {
                    string.append(" ");
                    string.append(this.grid[r][c]);
                } else {
                    string.append(this.grid[r][c]);
                    firstTime = true;
                }
            }
            firstTime = false;
            string.append("\n");
        }
        return string.toString();
    }
}

package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
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

    public void hint() {
        Solver solver = new Solver(this.currentConfig);
        ArrayList<Configuration> solverShortestList = solver.getShortestlist();
        if (!this.currentConfig.isSolution()) {
            if (solverShortestList.size() == 0) {
                alertObservers("No solution!");
            } else {
                this.currentConfig = (HoppersConfig) solverShortestList.get(1);
                this.grid = this.currentConfig.getGrid();
                alertObservers("Next step!");
            }
        } else {
            alertObservers("Already solved!");
        }
    }

    public void load(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        this.grid = this.currentConfig.getGrid();
        String[] file = filename.split("/");
        alertObservers("Loaded: " + file[2]);
    }

    public void load(File file) throws IOException {
        this.currentConfig = new HoppersConfig(file.getPath());
        this.grid = this.currentConfig.getGrid();
        alertObservers("Loaded: " + file.getName());
    }

    public void fail(String filename) {
        alertObservers("Failed to load: " + filename);
    }

    public void select(int row, int col) {
        if (this.frog == null) {
            if (this.grid[row][col] == 'G' || this.grid[row][col] == 'R') {
                this.frog = this.grid[row][col];
                this.savedRow = row;
                this.savedCol = col;
                alertObservers("Selected (" + col + ", " + row + ")");
            } else if (this.grid[row][col] == '.') {
                alertObservers("No frog at (" + col + ", " + row + ")");
            } else {
                alertObservers("Invalid selection (" + col + ", " + row + ")");
            }
        } else {
            // check if the desired location is empty
            if (this.grid[row][col] != '.') {
                this.frog = null;
                alertObservers("Can't jump from (" + this.savedCol + ", " + this.savedRow + ")  to (" +
                        col + ", " + row + ")");
                return;
            }
            // receives and verifies the row and col for the midpoint between two points
            int middleRow = -1;
            int middleCol = -1;
            if (row < this.savedRow) {
                middleRow = this.savedRow - 1;
            } else if (row > this.savedRow) {
                middleRow = row - 1;
            } else {
                middleRow = row;
            }
            if (col < this.savedCol) {
                middleCol = this.savedCol - 1;
            } else if (col > this.savedCol) {
                middleCol = col - 1;
            } else {
                middleCol = col;
            }
            if (row == this.savedRow) {
                middleCol = col - 2;
            } else if (col == this.savedCol) {
                middleRow = row - 2;
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
                alertObservers("Jumped from (" + this.savedCol + ", " + this.savedRow + ")  to (" +
                        col + ", " + row + ")");
            } else {
                this.frog = null;
                alertObservers("Can't jump from (" + this.savedCol + ", " + this.savedRow + ")  to (" +
                        col + ", " + row + ")");
            }
        }
    }

    public void reset(String filename) throws IOException {
        load(filename);
        alertObservers("Puzzle reset!");
    }

    public char[][] getGrid () {
        return this.grid;
    };

    public int getRowDIM() {
        return this.currentConfig.getRowDIM();
    }

    public int getColumnDIM() {
        return this.currentConfig.getColumnDIM();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        boolean firstTime = false;
        string.append("   ");
        for (int c= 0; c < (this.currentConfig.getColumnDIM()); c++){
            if (firstTime) {
                string.append(" ").append(c);
            } else {
                string.append(c);
                firstTime = true;
            }
        }
        firstTime = false;
        string.append("\n");
        string.append("  ");
        string.append("-".repeat(Math.max(0, (2 + 2 * this.currentConfig.getColumnDIM()) - 2)));
        string.append("\n");
        for (int c = 0; c < this.currentConfig.getRowDIM(); c++) {
            string.append(c).append("| ");
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

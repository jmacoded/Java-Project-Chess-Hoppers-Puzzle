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

/**
 * A hoppers-related class that provides the fountains for general UI to work
 *
 * @author Jamie Antal
 */
public class HoppersModel {
    /** A collection of observers representing the observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();
    /** A HopperConfig representing the current configuration */
    private HoppersConfig currentConfig;
    /** A Character representing a current picked-up frog in game */
    private Character frog;
    /** A char[][] representing the current status of board in game */
    private char[][] grid;
    /** A Int representing the first row input by the user */
    private int savedRow;
    /** A Int representing the first column input by the user */
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

    /**
     * Creates a New HoppersModel with given filename
     * @param filename Must be String, represents a file to extract information from
     * @throws IOException
     */
    public HoppersModel(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        this.grid = currentConfig.getGrid();
        this.frog = null;
        this.savedRow = -1;
        this.savedCol = -1;
    }

    /**
     * When called, it will create new Solver and looks for next step in solving the current puzzle. If found, it will
     * set the current Config to that and call alertObservers() with message. If it can't find any steps to solve the
     * puzzle, it will call alertObservers() with message
     */
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

    /**
     * When called, it will attempt to extract information from given filename and creates a new HopperConfig with
     * filename. If successful, it will set current Config to new HopperConfig and call alertObservers() with message.
     * If not successful, it will throw a IOException
     * @param filename Must be String, represents a file to extract from
     * @throws IOException
     */
    public void load(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        this.grid = this.currentConfig.getGrid();
        String[] file = filename.split("/");
        alertObservers("Loaded: " + file[2]);
    }

    /**
     * Same in every part as load(String filename), but only requires a file as parameter
     * @param file Must be File, represents a file to extract from
     * @throws IOException
     */
    public void load(File file) throws IOException {
        this.currentConfig = new HoppersConfig(file.getPath());
        this.grid = this.currentConfig.getGrid();
        alertObservers("Loaded: " + file.getName());
    }

    /**
     * When called, it will alert the observers with a message. Basically, its for when load() functions fails to load
     * a file
     * @param filename Must be String, represents a file's name
     */
    public void fail(String filename) {
        alertObservers("Failed to load: " + filename);
    }

    /**
     * When called for first time, it will take in row and column and see if there's frog present in the coordinates
     * on the board. If not, it will throw away the inputs. If there is a frog, it will save the coordinates.
     * When called for second time with saved row and column, it will take in row and column, and checks the given
     * information to see if it is valid or not. Finally, it will proceed to make a test config and compare it to
     * current Config's neighbors to see if the move is valid or not
     * @param row Must be Int, represents where to go at X axis of board in game
     * @param col Must be Int, represents where to go at Y axis of board in game
     */
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

    /**
     * When called, it will call the load() function to "reset" the game back to last used filename
     * @param filename Must be String, represents a file to extract information from
     * @throws IOException
     */
    public void reset(String filename) throws IOException {
        load(filename);
        alertObservers("Puzzle reset!");
    }

    /**
     * Gets and returns the grid of the game
     * @return A char[][] representing the current status of board in game
     */
    public char[][] getGrid () {
        return this.grid;
    };

    /**
     * Gets and returns the romDIM of the game
     * @return A Int representing the range of rows in game
     */
    public int getRowDIM() {
        return this.currentConfig.getRowDIM();
    }

    /**
     * Gets and returns the columnDIM of the game
     * @return A Int representing the range of columns in game
     */
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

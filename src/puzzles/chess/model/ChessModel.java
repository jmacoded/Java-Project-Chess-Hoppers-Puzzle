package puzzles.chess.model;

import puzzles.chess.solver.Chess;
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
 * The model for the chess solitaire puzzle
 *
 * @author Teju Rajbabu
 */
public class ChessModel {

    /** grid of characters, represents chess board */
    private char[][] grid;
    /** The starting configuration of the loaded file */
    private ChessConfig init;
    /** Saved column for first selection */
    private int firstCol;
    /** Saved row for first selection */
    private int firstRow;
    /** List of configurations of the 'optimal' path to the solution*/
    private ArrayList<Configuration> shortestlist;
    /** All next possible moves for the configuration */
    private Collection<Configuration> nextmoves;
    /** The number of columns for that configuration */
    private int columns;
    /** The number of rows for that configuration */
    private int rows;
    /** the current configuration */
    private ChessConfig config;

    /** the non-path file name */
    private String filepath;

    /**
     * An empty cell
     */
    char EMPTY = '.';

    /**
     * A list of pieces of a chess board
     */
    private static final Character[] pieces =
            {'B', 'K', 'N', 'P', 'Q', 'R'};
    /**
     * A cell occupied with a bishop
     */
    char BISHOP = 'B';
    /**
     * A cell occupied with a king
     */
    char KING = 'K';
    /**
     * A cell occupied with a knight
     */
    char KNIGHT = 'N';
    /**
     * A cell occupied with a pawn
     */
    char PAWN = 'P';
    /**
     * A cell occupied with a queen
     */
    char QUEEN = 'Q';
    /**
     * A cell occupied with a rook
     */
    char ROOK = 'R';

    /** the collection of observers of this model */
    private final List<Observer<ChessModel, String>> observers = new LinkedList<>();


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<ChessModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Read in the file of the chess board, initialise a new chess
     * configuration, gets rows and columns, and set up waiting room.
     * @param filename the name of the file to read from
     * @throws IOException if the file is not found or there are errors reading
     */
    public ChessModel(String filename) throws IOException {
            this.config = new ChessConfig(filename);
            this.init = new ChessConfig(filename);
            this.rows = config.getRowDIM();
            this.grid = config.getGrid();
            this.columns = config.getColumnDIM();
            this.firstCol = -1;
            this.firstRow = -1;
    }

    /**
     * Constructor for ChessPTUI, takes in the filepath and
     * makes a new Chess configuration with that file. Alerts Load
     * @param filepath full file path to access file
     * @throws IOException if the file is not found or there are errors reading
     */

    public void load(String filepath) throws IOException{
        config = new ChessConfig(filepath);
        this.filepath = filepath;
        String[] filename = filepath.split("/");
        alertObservers("Loaded: " + filename[2]);
    }

    /**
     * Constructor for ChessGUI, takes in the filepath and
     * makes a new Chess configuration with that file. Alerts Load
     * @param filepath full file path to access file
     * @throws IOException if the file is not found or there are errors reading
     */
    public void load(File filepath) throws IOException{
        config = new ChessConfig(filepath.getPath());
        this.filepath = filepath.getName();
        alertObservers("Loaded: " + filepath.getName());
    }

    /**
     * The Waiting room, stores the first coordinates if the selection is valid,
     * then enters the move if thhe second coordinates are legal.
     * @param x1 x coordinate of piece
     * @param y1 y coordinate of piece
     */

    public void waitingRoom (int x1, int y1){
        if (firstRow == -1 & firstCol == -1){
            validSelection(x1, y1);

        } else {
            enterMove(firstRow, firstCol, x1, y1);
            firstRow = -1;
            firstCol = -1;
        }
    }

    /**
     * Gets all possible next moves from the board state. Gets
     * the pieces from the given coordinates. Checks if the move from
     * one piece to the other is valid and captures if it is. Also checks
     * if the move isn't valid and if the first piece is invalid.
     * @param x1 x coordinate of first piece
     * @param y1 y coordinate of first piece
     * @param x2 x coordinate of second piece
     * @param y2 y coordinate of second piece
     */
    public void enterMove(int x1, int y1, int x2, int y2){
        nextmoves = config.getNeighbors();
        char piece = config.getGrid()[x1][y1];
        char piece2 = config.getGrid()[x2][y2];
        if (config.cellCheck(piece)){
            config.getGrid()[x1][y1] = EMPTY;
            config.getGrid()[x2][y2] = piece;
            if (nextmoves.contains(config)){
                // successful capture
                alertObservers("Captured from (" + x1 + ", " + y1 + ")  to (" + x2 + ", " + y2 + ")");
            } else {
                config.getGrid()[x1][y1] = piece;
                config.getGrid()[x2][y2] = piece2;
                alertObservers("Can't capture from (" + x1 + ", " + y1 + ")  to (" + x2 + ", " + y2 + ")");
            }
        } else {
            alertObservers("Invalid selection (" + x1 + ", " + y1 + ")");
        }
    }

    /**
     * Checks the first selection to see if it is a valid piece
     *
     * @param x1 x coordinate of piece
     * @param y1 y coordinate of piece
     */
    public void validSelection(int x1, int y1) {
        char piece = this.config.getGrid()[x1][y1];
        if (!config.isSolution()){
            if( this.config.cellCheck(piece)){
                alertObservers("Selected (" + x1 + ", " + y1 + ")");
                firstRow = x1;
                firstCol = y1;
            } else {
                alertObservers("Invalid selection (" + x1 + ", " + y1 + ")");
            }
        } else {
            this.alertObservers("Already solved!");
        }
    }

    /**
     * Resets the board
     * @param file the name of the file to read from
     * @throws IOException if the file is not found or there are errors reading
     */

    public void reset(String file) throws IOException{
        load(file);
        alertObservers("Puzzle reset!");
    }

    /**
     * Gets the columns of the current config
     * @return config's columns
     */

    public int getColumns() {
        return config.getColumnDIM();
    }

    /**
     * Gets the rows of the current config
     * @return config's rows
     */
    public int getRows() {
        return config.getRowDIM();
    }

    /**
     * Gets the grid of the current config
     * @return config's grid
     */
    public char[][] getGrid() {
        return config.getGrid();
    }

    /**
     * Solves the configuration to get a list of the path to the solution.
     * uses the path to get the most efficient next step of the puzzle and
     * changes the current configuration to that step.
     */

    public void solving(){
        Solver solver = new Solver(this.config);
        shortestlist = solver.getShortestlist();
        if (!config.isSolution()){
            if (!shortestlist.isEmpty()){
                this.config = (ChessConfig) shortestlist.get(1);
                this.alertObservers("Next step!");
            } else {
                this.alertObservers("No solution");
            }
        } else {
            this.alertObservers("Already solved!");
        }

    }

    /**
     * this is called when the file can't be read, updates message to fail
     */
    public void fail(String file){
        this.alertObservers("Failed to load: " + file);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("   ");
        for (int c= 0; c < this.config.getColumnDIM(); c++){
            string.append(c);
            if (c < this.config.getColumnDIM() -1){
                string.append(" ");
            }
        }
        string.append("\n");
        string.append("  ");
        for (int c = 0; c < (2* this.config.getColumnDIM()); c++){
            string.append("-");
        }
        string.append("\n");
        for (int r = 2; r < this.config.getRowDIM() + 2; r++) {
            string.append(r - 2 + "| ");
            for (int c = 2; c < this.config.getColumnDIM() + 2; c++) {
                string.append(this.config.getCell(r-2, c-2));
                if (c < this.config.getColumnDIM() + 1){
                    string.append(" ");
                }
            }
            string.append("\n");
        }
        return string.toString();
    }
}

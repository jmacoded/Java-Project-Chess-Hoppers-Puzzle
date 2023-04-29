package puzzles.chess.model;

import puzzles.chess.solver.Chess;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChessModel {
    /** the collection of observers of this model */
    private final List<Observer<ChessModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private char[][] grid;

    private ChessConfig init;
    private ChessConfig primary;

    private ArrayList<Configuration> shortestlist;

    private Collection<Configuration> nextmoves;

    private int columns;
    private int rows;

    private ChessConfig config;
    private String filepath;

    char EMPTY = '.';

    private static final Character[] pieces =
            {'B', 'K', 'N', 'P', 'Q', 'R'};
    char BISHOP = 'B';
    char KING = 'K';
    char KNIGHT = 'N';
    char PAWN = 'P';
    char QUEEN = 'Q';
    char ROOK = 'R';


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

    public ChessModel(String filename) throws IOException {
            this.config = new ChessConfig(filename);
            this.init = new ChessConfig(filename);
            this.grid = config.getGrid();
            this.rows = config.getRowDIM();
            this.columns = config.getColumnDIM();
    }

    public void load(String filepath) throws IOException{
        config = new ChessConfig(filepath);
        this.filepath = filepath;
        String[] filename = filepath.split("/");
        alertObservers("Loaded: " + filename[2]);
    }

    public int enterMove(int x1, int y1, int x2, int y2){
        nextmoves = config.getNeighbors();
        char piece = config.getGrid()[x1][y1];
        char piece2 = config.getGrid()[x2][y2];
        if (config.cellCheck(piece)){
            config.getGrid()[x1][y1] = EMPTY;
            config.getGrid()[x2][y2] = piece;
            if (nextmoves.contains(config)){
                // successful capture
                alertObservers("> Captured from (" + x1 + ", " + y1 + ")  to (" + x2 + ", " + y2 + ")");

                return 0;
            } else {
                config.getGrid()[x1][y1] = piece;
                config.getGrid()[x2][y2] = piece2;
                alertObservers("> Can't capture from (" + x1 + ", " + y1 + ")  to (" + x2 + ", " + y2 + ")");
                return 1;
            }
        } else {
            alertObservers("> Invalid selection (" + x1 + ", " + y1 + ")");
            return -1;
        }
    }

    public boolean validSelection(int x1, int y1) {
        char piece = this.config.getGrid()[x1][y1];
        if( this.config.cellCheck(piece)){
            alertObservers("Selected (" + x1 + ", " + y1 + ")");
            return true;
        } else {
            alertObservers("Invalid selection (" + x1 + ", " + y1 + ")");
            return false;
        }
    }

    public void reset(String file) throws IOException{
        load(file);
        alertObservers("Puzzle reset!");
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public char[][] getGrid() {
        return grid;
    }

    public void solving(){
        Solver solver = new Solver(this.config);
        shortestlist = solver.getShortestlist();
        this.config = (ChessConfig) shortestlist.get(1);
        this.alertObservers("Next step!");
    }
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

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
    private ChessConfig currentConfig;

    private ArrayList<ChessConfig> shortSolution;

    private char[][] grid;

    private ChessConfig init;
    private ChessConfig primary;

    private ArrayList<Configuration> shortestlist;

    private Collection<Configuration> nextmoves;

    private int columns;
    private int rows;

    private static ChessModel model;
    private ChessConfig config;

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
//            System.out.println(currentConfig);
//            currentConfig.getGrid();
//            this.grid = currentConfig.getGrid();
    }

    public ChessModel load(String filepath) throws IOException{
        ChessModel chessModel = new ChessModel(filepath);
        String[] filename = filepath.split("/");
        System.out.println("Loaded: " + filename[2]);
        System.out.println(chessModel);
        return chessModel;

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
                return 0;
            } else {
                config.getGrid()[x1][y1] = piece;
                config.getGrid()[x2][y2] = piece2;
                return 1;
            }
        } else {
            return -1;
        }
    }

    public boolean validSelection(int x1, int y1) {
        char piece = this.config.getGrid()[x1][y1];
        return this.config.cellCheck(piece);
    }

    public void solving(){
        Solver solver = new Solver(this.config);
        shortestlist = solver.getShortestlist();
        this.config = (ChessConfig) shortestlist.get(1);
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

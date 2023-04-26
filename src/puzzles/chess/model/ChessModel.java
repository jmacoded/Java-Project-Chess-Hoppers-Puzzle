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
        try {
            init = new ChessConfig(filename);
            primary = new ChessConfig(filename);
            String[] file = filename.split("/");
            String message = "Loaded: " + file[2];
            System.out.println(message);
            currentConfig = new ChessConfig(filename);
            System.out.println(currentConfig);
            currentConfig.getGrid();
            this.grid = currentConfig.getGrid();
        } catch (IOException e ){}
    }

    public void enterMove(int x1, int y1, int x2, int y2){
        nextmoves = currentConfig.getNeighbors();
        char piece = currentConfig.getGrid()[x1][y1];
        char piece2 = currentConfig.getGrid()[x2][y2];
        if (currentConfig.cellCheck(piece)){
            if (currentConfig.cellCheck(piece)){
                currentConfig.getGrid()[x1][y1] = EMPTY;
                currentConfig.getGrid()[x2][y2] = piece;
                if (nextmoves.contains(currentConfig)){
                    init = currentConfig;
                    System.out.println("> Captured from (" + x1+ ", " + y1 + ") to (" + x2 + ", " + y2 + ")");
                    System.out.println(init);
                    // successful capture
                } else {
                    System.out.println("> Can't capture from  (" + x1+ ", " + y1 + ") to (" + x2 + ", " + y2 + ")");
                    System.out.println(init);
                }
            } else {
                System.out.println("> Can't capture from  (" + x1+ ", " + y1 + ") to (" + x2 + ", " + y2 + ")");
                System.out.println(init);
            }
        } else {
            System.out.println("> Invalid selection (" + + x1+ ", " + y1 + ")");
            System.out.println(init);
        }
    }

    public boolean validSelection(int x1, int y1) {
        char piece = currentConfig.getGrid()[x1][y1];
        System.out.println("> Selected at (" + x1 + ", " + y1 + ")");
        System.out.println(currentConfig);
        if (currentConfig.cellCheck(piece)) {
            return true;
        }
        return false;
    }

    public void solving(){
        Solver solver = new Solver(currentConfig);
        shortestlist = solver.getShortestlist();
        System.out.println("> Next step!");
        currentConfig = (ChessConfig) shortestlist.get(1);
        System.out.println(shortestlist.get(1));
    }


    public void reset(){
        currentConfig = primary;
        System.out.println("Puzzle reset!");
        System.out.println(currentConfig);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int c= 0; c < currentConfig.getColumnDIM(); c++){
            string.append(c);
        }
        for (int r = 2; r < currentConfig.getRowDIM() + 2; r++) {
            for (int c = 2; c < currentConfig.getColumnDIM() + 2; c++) {
                string.append(currentConfig.getCell(r, c));
                string.append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }
}

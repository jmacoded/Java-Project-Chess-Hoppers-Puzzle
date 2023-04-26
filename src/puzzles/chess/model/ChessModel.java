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
        try {
            init = new ChessConfig(filename);
            primary = new ChessConfig(filename);
            String[] file = filename.split("/");
            String message = "Loaded: " + file[2];
            System.out.println(message);
            currentConfig = new ChessConfig(filename);
            model = new ChessModel(currentConfig);
//            System.out.println(currentConfig);
//            currentConfig.getGrid();
//            this.grid = currentConfig.getGrid();
        } catch (IOException e ){

            fail(filename);}
    }
    public ChessModel(ChessConfig config){
        this.grid = config.getGrid();
        this.config = config;
        this.columns = config.getColumnDIM();
        this.rows = config.getRowDIM();
    }

    public void enterMove(int x1, int y1, int x2, int y2){
        nextmoves = currentConfig.getNeighbors();
        char piece = currentConfig.getGrid()[x1][y1];
        if (currentConfig.cellCheck(piece)){
            if (currentConfig.cellCheck(piece)){
                currentConfig.getGrid()[x1][y1] = EMPTY;
                currentConfig.getGrid()[x2][y2] = piece;
                if (nextmoves.contains(currentConfig)){
                    ChessModel initModel = new ChessModel(currentConfig);
                    System.out.println("Captured from (" + x1+ ", " + y1 + ")  to (" + x2 + ", " + y2 + ")");
                    System.out.println(initModel);
                    // successful capture
                } else {
                    System.out.println("Can't capture from  (" + x1+ ", " + y1 + ")  to (" + x2 + ", " + y2 + ")");
                    model = new ChessModel(init);
                    System.out.println(model);
                }
            } else {
                System.out.println("Can't capture from  (" + x1+ ", " + y1 + ")  to (" + x2 + ", " + y2 + ")");
                model = new ChessModel(init);
                System.out.println(model);
            }
        } else {
            System.out.println("Invalid selection (" + + x1+ ", " + y1 + ")");
            model = new ChessModel(init);
            System.out.println(model);
        }
    }

    public boolean validSelection(int x1, int y1) {
        char piece = currentConfig.getGrid()[x1][y1];
        model = new ChessModel(currentConfig);
        if (currentConfig.cellCheck(piece)) {
            System.out.println("Selected at (" + x1 + ", " + y1 + ")");
            System.out.println(model);
            return true;
        }
        System.out.println("Invalid selection (" + x1 + ", " + y1 + ")");
        System.out.println(model);
        return false;
    }

    public void solving(){
        Solver solver = new Solver(currentConfig);
        shortestlist = solver.getShortestlist();
        System.out.println("Next step!");
        currentConfig = (ChessConfig) shortestlist.get(1);
        model = new ChessModel(currentConfig);
        System.out.println(model);
    }


    public void reset(){
        currentConfig = primary;
        System.out.println("Puzzle reset!");
        model = new ChessModel(currentConfig);
        System.out.println(model);
    }
    public void fail(String string){
        System.out.println("Failed to load: " + string);
        System.out.println(model);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("   ");
        for (int c= 0; c < model.config.getColumnDIM(); c++){
            string.append(c + " ");
        }
        string.append("\n");
        string.append("  ");
        for (int c = 0; c < (2* model.columns); c++){
            string.append("-");
        }
        string.append("\n");
        for (int r = 2; r < model.rows + 2; r++) {
            string.append(r - 2 + "| ");
            for (int c = 2; c < model.columns + 2; c++) {
                string.append(model.config.getCell(r-2, c-2));
                string.append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }
}

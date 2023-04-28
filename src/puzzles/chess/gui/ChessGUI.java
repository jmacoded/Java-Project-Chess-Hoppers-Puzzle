package puzzles.chess.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;
import puzzles.hoppers.model.HoppersModel;
import javafx.scene.control.Label;

import java.io.IOException;


public class ChessGUI extends Application implements Observer<ChessModel, String> {
    private ChessModel model;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    private Stage stage;

    private Label label;

    private String file;
    char BISHOP = 'B';
    char KING = 'K';
    char KNIGHT = 'N';
    char PAWN = 'P';
    char QUEEN = 'Q';
    char ROOK = 'R';

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image bishop = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"bishop.png"));

    private Image knight = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"knight.png"));
    private Image king = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"king.png"));
    private Image pawn = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"pawn.png"));
    private Image queen = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"queen.png"));
    private Image rook = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"rook.png"));

    private GridPane board;
    /** a definition of light and dark and for the button backgrounds */
    private static final Background LIGHT =
            new Background( new BackgroundFill(Color.WHITE, null, null));
    private static final Background DARK =
            new Background( new BackgroundFill(Color.MIDNIGHTBLUE, null, null));

    @Override
    public void init() {
        // get the file name from the command line
        String filename = getParameters().getRaw().get(0);
        file = filename.split("/")[2];
        try {
            model = new ChessModel(filename);
            this.model.addObserver(this);
        } catch (IOException e){}
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Chess GUI");
        BorderPane borderPane = new BorderPane();
        Label label = new Label("Loaded: " + file);
        borderPane.setTop(label);
        label.setAlignment(Pos.CENTER_RIGHT);

        HBox hbox = buttons();
        borderPane.setBottom(hbox);
        hbox.setAlignment(Pos.CENTER);

        board = board();
        borderPane.setCenter(board);
        board.setAlignment(Pos.CENTER);

        Button button = new Button();
        button.setGraphic(new ImageView(bishop));
        button.setBackground(LIGHT);
        button.setMinSize(ICON_SIZE, ICON_SIZE);
        button.setMaxSize(ICON_SIZE, ICON_SIZE);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    private GridPane board(){
        char[][] grid = model.getGrid();
        GridPane gridPane = new GridPane();
        for (int row=0; row< model.getRows(); ++row) {
            for (int col = 0; col < model.getColumns(); ++col) {
                if (grid[row][col] == BISHOP) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(bishop));
                    setButton(button, row, col);
                    gridPane.add(button, col, row);
                } else if (grid[row][col] == KNIGHT) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(knight));
                    setButton(button, row, col);
                    gridPane.add(button, col, row);
                } else if (grid[row][col] == KING) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(king));
                    setButton(button, row, col);
                    gridPane.add(button, col, row);
                } else if (grid[row][col] == PAWN) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(pawn));
                    setButton(button, row, col);
                    gridPane.add(button, col, row);
                } else if (grid[row][col] == QUEEN) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(queen));
                    setButton(button, row, col);
                    gridPane.add(button, col, row);
                } else if (grid[row][col] == ROOK) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(rook));
                    setButton(button, row, col);
                    gridPane.add(button, col, row);
                } else {
                    Button button = new Button();
                    setButton(button, row, col);
                    gridPane.add(button, col, row);
                }
            }
        }
        return gridPane;
    }
    private HBox buttons(){
        HBox hbox = new HBox();
        Button Load = new Button("Load");
        Button Reset = new Button("Reset");
        Button Hint = new Button("Hint");
        hbox.getChildren().addAll(Load, Reset, Hint);
        return hbox;
    }

    private void setButton(Button button,int row, int col) {
        if ((row + col) % 2 == 0) {
            button.setBackground(LIGHT);
        } else {
            button.setBackground(DARK);
        }
        button.setMinSize(ICON_SIZE, ICON_SIZE);
        button.setMaxSize(ICON_SIZE, ICON_SIZE);

    }

    @Override
    public void update(ChessModel chessModel, String msg) {

        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

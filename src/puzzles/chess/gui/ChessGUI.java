package puzzles.chess.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;
import puzzles.hoppers.model.HoppersModel;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


/**
 * The graphical user interface to the Chess solitaire game model
 *
 * @author Teju Rajbabu
 */
public class ChessGUI extends Application implements Observer<ChessModel, String> {
    /** View/Controller access to model */
    private ChessModel model;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;
    /** Stage for GUI*/
    private Stage stage;
    /** Top label, changes for controller inputs*/
    private Label label;
    /** the name of the file to read from*/
    private String filepath;
    /** full file path to access file */
    private String file;
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

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    /** Image of Bishop */
    private Image bishop = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"bishop.png"));
    /** Image of Knight */
    private Image knight = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"knight.png"));
    /** Image of King */
    private Image king = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"king.png"));
    /** Image of Pawn */
    private Image pawn = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"pawn.png"));
    /** Image of Queen */
    private Image queen = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"queen.png"));
    /** Image of Rook */
    private Image rook = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"rook.png"));
    /** Chess board with pieces*/
    private GridPane board;
    /** Border pane of GUI to hold label, chess board, and interactive buttons */
    private BorderPane borderPane;

    /** a definition of light and dark and for the button backgrounds */
    private static final Background LIGHT =
            new Background( new BackgroundFill(Color.WHITE, null, null));
    private static final Background DARK =
            new Background( new BackgroundFill(Color.MIDNIGHTBLUE, null, null));

    @Override
    public void init() {
        // get the file name from the command line
        filepath = getParameters().getRaw().get(0);
        file = filepath.split("/")[2];
        try {
            model = new ChessModel(filepath);
            this.model.addObserver(this);
        } catch (IOException e){}
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Chess GUI");
        borderPane = new BorderPane();
        label = new Label("Loaded: " + file);
        HBox labelbox = new HBox();
        labelbox.getChildren().add(label);
        borderPane.setTop(labelbox);
        labelbox.setAlignment(Pos.CENTER);


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

    /**
     * A helper method the builds the chess board to return
     *
     * @return the chess board
     */
    private GridPane board(){
        char[][] grid = model.getGrid();
        board = new GridPane();
        for (int row=0; row< model.getRows(); ++row) {
            for (int col = 0; col < model.getColumns(); ++col) {
                if (grid[row][col] == BISHOP) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(bishop));
                    setButton(button, row, col);
                    board.add(button, col, row);
                } else if (grid[row][col] == KNIGHT) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(knight));
                    setButton(button, row, col);
                    board.add(button, col, row);
                } else if (grid[row][col] == KING) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(king));
                    setButton(button, row, col);
                    board.add(button, col, row);
                } else if (grid[row][col] == PAWN) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(pawn));
                    setButton(button, row, col);
                    board.add(button, col, row);
                } else if (grid[row][col] == QUEEN) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(queen));
                    setButton(button, row, col);
                    board.add(button, col, row);
                } else if (grid[row][col] == ROOK) {
                    Button button = new Button();
                    button.setGraphic(new ImageView(rook));
                    setButton(button, row, col);
                    board.add(button, col, row);
                } else {
                    Button button = new Button();
                    setButton(button, row, col);
                    board.add(button, col, row);
                }
            }
        }
        return board;
    }

    /**
     * A helper method that builds the three buttons, Load, Reset, and Hint
     * with functionality into a HBOX
     *
     * @return Load, Reset, and Hint buttons in a HBox
     */
    private HBox buttons(){
        HBox hbox = new HBox();
        Button Load = new Button("Load");
        Load.setOnAction(event -> loader());
        Button Reset = new Button("Reset");
        Reset.setOnAction(event -> reseter());
        Button Hint = new Button("Hint");
        Hint.setOnAction(event -> hinter());
        hbox.getChildren().addAll(Load, Reset, Hint);
        return hbox;
    }

    /**
     * A helper method to reset the model any time the reset
     * button is pressed.
     */
    public void reseter(){
        try {
            this.model.reset(filepath);
        }catch (IOException e){}
    }

    /**
     * A helper method to call solving for the model to get
     * the next step in the puzzle
     */
    public void hinter(){
        this.model.solving();
    }

    /**
     * A helper method to load a new file and change the board to that
     * new file
     */
    public void loader(){
        FileChooser chooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "chess";  // or "hoppers"
        chooser.setInitialDirectory(new File(currentPath));
        File file = chooser.showOpenDialog(stage);
        filepath = file.getPath();
        try{
            this.model.load(file);
        } catch (IOException e){}
    }

    /**
     * A helper method to cut down text, Makes the button have either
     * a light or a dark background depending on the row + col combination
     * Sets action to call model's waiting room to select buttons.
     * Buttons are the right size.
     * @param button Chess square with or without piece
     * @param row row on Chess board
     * @param col column on Chess board
     */
    private void setButton(Button button,int row, int col) {
        if ((row + col) % 2 == 0) {
            button.setBackground(LIGHT);
        } else {
            button.setBackground(DARK);
        }
        button.setMinSize(ICON_SIZE, ICON_SIZE);
        button.setMaxSize(ICON_SIZE, ICON_SIZE);
        button.setOnAction(e -> model.waitingRoom(row, col));
    }

    @Override
    public void update(ChessModel chessModel, String msg) {

        board = board();
        borderPane.setCenter(board);
        this.stage.sizeToScene();  // when a different sized puzzle is loaded
        label.setText(msg);
    }

    /**
     * Starts up the GUI interface
     *
     * @param args the filename to launch the application
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}

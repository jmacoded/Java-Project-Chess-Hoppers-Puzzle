package puzzles.hoppers.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import puzzles.chess.model.ChessModel;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    private HoppersModel model;
    private String savedFilePath;
    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));
    private Stage stage;
    private BorderPane borderPane;
    private Label label;
    private GridPane hoppersBoard;

    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.savedFilePath = filename;
        try {
            this.model = new HoppersModel(filename);
            this.model.addObserver(this);
        } catch (IOException e){}
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        this.stage.setTitle("Hoppers GUI");

        this.borderPane = new BorderPane();

        this.label = new Label("Loaded: " + this.savedFilePath);
        HBox hBox = new HBox();
        hBox.getChildren().add(this.label);
        this.borderPane.setTop(hBox);
        hBox.setAlignment(Pos.CENTER);


        hoppersBoard();
        this.borderPane.setCenter(this.hoppersBoard);
        this.hoppersBoard.setAlignment(Pos.CENTER);

        HBox hbox = buttons();
        this.borderPane.setBottom(hbox);
        hbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(this.borderPane);
        stage.setScene(scene);
        stage.show();
    }

    private void hoppersBoard() {
        this.hoppersBoard = new GridPane();
        char[][] grid = this.model.getGrid();
        for (int c = 0; c < this.model.getColumnDIM(); c++) {
            for (int r = 0; r < this.model.getRowDIM(); r++) {
                Button button = new Button();
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                if (grid[r][c] == 'G') {
                    button.setGraphic(new ImageView(this.greenFrog));
                    this.hoppersBoard.add(button, r, c);
                }
                if (grid[r][c] == '.') {
                    button.setGraphic(new ImageView(this.lilyPad));
                    this.hoppersBoard.add(button, r, c);
                }
                if (grid[r][c] == 'R') {
                    button.setGraphic(new ImageView(this.redFrog));
                    this.hoppersBoard.add(button, r, c);
                }
                if (grid[r][c] == '*') {
                    button.setGraphic(new ImageView(this.water));
                    this.hoppersBoard.add(button, r, c);
                }
                final int finalRow = r;
                final int finalCol = c;
                button.setOnAction(event -> {
                    this.model.select(finalRow, finalCol);
                });
            }
        }
    }

    private HBox buttons(){
        HBox hbox = new HBox();
        Button load = new Button("Load");
        load.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            currentPath += File.separator + "data" + File.separator + "hoppers";
            chooser.setInitialDirectory(new File(currentPath));
            File file = chooser.showOpenDialog(stage);
            try {
                this.model.load(file);
                this.savedFilePath = file.getPath();
            } catch (IOException ioException) {
                this.model.fail(file.getPath());
            }
        });
        Button reset = new Button("Reset");
        reset.setOnAction(event -> {
            try {
                this.model.reset(this.savedFilePath);
            } catch (IOException ignored) {}
        });
        Button hint = new Button("Hint");
        hint.setOnAction(event -> {
            this.model.hint();
        });
        hbox.getChildren().addAll(load, reset, hint);
        return hbox;
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        hoppersBoard();
        this.borderPane.setCenter(this.hoppersBoard);
        this.stage.sizeToScene();  // when a different sized puzzle is loaded
        this.label.setText(msg);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}

package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import puzzles.chess.ptui.ChessPTUI;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;
    private boolean initialized;
    private PrintWriter out;
    private String savedFilePath;

    public void init(String filename) throws IOException {
        this.initialized = false;
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        this.savedFilePath = filename;
        displayHelp();
    }

    public void start( PrintWriter out ) {
        this.out = out;
        this.initialized = true;
        // not sure if needed to put more in here
    }

    @Override
    public void update(HoppersModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                } else if (words[0].startsWith("h")) {
                    if (this.model.hint()) {
                        System.out.println("Next step!");
                    } else {
                        System.out.println("No solution!");
                    }
                    System.out.println(this.model);
                } else if (words[0].startsWith("l")) {
                    try {
                        this.model = this.model.load(words[1]);
                    } catch (IOException ioException) {
                        System.out.println("Failed to load: " + words[1]);
                        System.out.println(this.model);
                    }
                } else if (words[0].startsWith("s")) {
                    int result = this.model.select(Integer.parseInt(words[2]), Integer.parseInt(words[1]));
                    if (result == 1) {
                        System.out.println("Selected (" + words[1] + ", " + words[2] + ")");
                    } else if (result == 2) {
                        System.out.println("No frog at (" + words[1] + ", " + words[2] + ")");
                    } else if (result == 3) {
                        System.out.println("Jumped from (" +
                                this.model.getSavedCol() + ", " + this.model.getSavedRow() +
                                ")  to (" +
                                words[1] + ", " + words[2] + ")");
                    } else if (result == 4) {
                        System.out.println("Can't jump from (" +
                                this.model.getSavedCol() + ", " + this.model.getSavedRow() +
                                ")  to (" +
                                words[1] + ", " + words[2] + ")");
                    }
                    System.out.println(this.model);
                } else if (words[0].startsWith("r")) {
                    try {
                        this.model = this.model.load(this.savedFilePath);
                    } catch (IOException ignored) {}
                    System.out.println("Puzzle reset!");
                    System.out.println(this.model);
                } else {
                    displayHelp();
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}

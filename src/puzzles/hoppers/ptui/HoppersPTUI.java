package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import puzzles.chess.ptui.ChessPTUI;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A hoppers-related class with functions that build the PTUI version of Hoppers game
 *
 * @author Jamie Antal
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {
    /** Represents the model */
    private HoppersModel model;
    /** Represents the saved file */
    private String savedFilePath;

    /**
     * Before starting up the PTUI, init() will attempt to create a new HoppersModel with given file, add the PTUI
     * to model's observers, saves the file, and prints out the puzzle
     * @param filename Must be String, represents a file to extract information from
     * @throws IOException
     */
    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        this.savedFilePath = filename;
        String[] file = filename.split("/");
        System.out.println("Loaded: " + file[2]);
        System.out.println(this.model);
        displayHelp();
    }

    @Override
    public void update(HoppersModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    /**
     * When the user inputs something that's doesn't match with commands of the game, it will print out information on
     * how to call proper commands of the game
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * When called, it will prints and awaits for user's input to proceed. Here's the breakdown of commands and their
     * functions:
     * q(uit): will quit and closes the program.
     * h(int): will advance the puzzle one step closer to the solution if possible
     * l(oad): Must be called with given file, will attempt to load the puzzle from given file
     * s(elect): Must be called with two numbers, will call model's select() function
     * r(eset): Will reset the current puzzle back to starting position
     * It will check the first character of input, so nevertheless if you make a typo like hit, it will assume that you
     * are calling hint. If you didn't input any of proper commands, it will call displayHelp() function
     */
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
                    this.model.hint();
                } else if (words[0].startsWith("l")) {
                    try {
                        this.model.load(words[1]);
                        this.savedFilePath = words[1];
                    } catch (IOException ioException) {
                        this.model.fail(words[1]);
                    }
                } else if (words[0].startsWith("s")) {
                    this.model.select(Integer.parseInt(words[2]), Integer.parseInt(words[1]));
                } else if (words[0].startsWith("r")) {
                    try {
                        this.model.reset(this.savedFilePath);
                    } catch (IOException ignored) {}
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

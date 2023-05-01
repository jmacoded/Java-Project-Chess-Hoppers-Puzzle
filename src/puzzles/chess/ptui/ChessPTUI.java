package puzzles.chess.ptui;

import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;

import java.io.IOException;
import java.util.Scanner;


/**
 * A Plain-Text user interface for the Chess solitaire puzzle
 *
 * @author Teju Rajbabu
 */
public class ChessPTUI implements Observer<ChessModel, String> {
    /** View/Controller access to model */
    private ChessModel model;
    /** the name of the file to read from */
    private String file;

    /**
     * Create the Chess model and register this object as an observer
     * of it. prints the first model load.
     * @param filename
     * @throws IOException
     */
    public void init(String filename) throws IOException {
        this.model = new ChessModel(filename);
        this.model.addObserver(this);
        this.file = filename;
        String[] filething = filename.split("/");
        System.out.println("Loaded: " + filething[2]);
        System.out.println(model);
        displayHelp();
    }

    @Override
    public void update(ChessModel model, String data) {
        System.out.println(data);
        System.out.println(model);
    }

    /**
     * prints out the possible inputs for the PTUI
     */

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * Reads the controller input line by line and executes the relevant
     * action with what the controller inputs.
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith("q")) {
                    break;
                }
                else if (words[0].startsWith("s")) {
                    model.waitingRoom(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                }
                else if (words[0].startsWith("r")) {
                    try{
                        this.model.reset(file);
                    } catch (IOException e){};
                }
                else if (words[0].startsWith("h")){
                    this.model.solving();
                } else if (words[0].startsWith("l")) {
                    try {
                        this.model.load(words[1]);
                    } catch (IOException e) {
                        this.model.fail(words[1]);
                    }

                } else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * Start up the console application.
     * @param args The file name to create the model
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ChessPTUI filename");
        } else {
            try {
                ChessPTUI ptui = new ChessPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}


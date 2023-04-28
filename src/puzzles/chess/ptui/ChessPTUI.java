package puzzles.chess.ptui;

import puzzles.common.Observer;
import puzzles.chess.model.ChessModel;

import java.io.IOException;
import java.util.Scanner;

public class ChessPTUI implements Observer<ChessModel, String> {
    private ChessModel model;

    private String file;

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
                if (words[0].startsWith("q")) {
                    break;
                }
                else if (words[0].startsWith("s")) {
                    if (model.validSelection(Integer.parseInt(words[1]), Integer.parseInt(words[2]))) {
                        System.out.println("Selected (" + words[1] + ", " + words[2] + ")");
                        System.out.println(this.model);
                        String line2 = in.nextLine();
                        String[] words2 = line2.split("\\s+");
                        if (words2[0].startsWith("s")) {
                            int result = this.model.enterMove(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Integer.parseInt(words2[1]), Integer.parseInt(words2[2]));
                            if (result == 0) {
                                System.out.println("> Captured from (" + words[1] + ", " + words[2] + ")  to (" + words2[1] + ", " + words2[2] + ")");
                                System.out.println(this.model);
                            } else if (result == 1){
                                System.out.println("> Can't capture from (" + words[1] + ", " + words[2] + ")  to (" + words2[1] + ", " + words2[2] + ")");
                                System.out.println(this.model);
                            } else if (result == -1){
                                System.out.println("> Invalid selection (" + words[1] + ", " + words[2] + ")");
                                System.out.println(this.model);
                            }
                        }
                    } else {
                        System.out.println("Invalid selection (" + words[1] + ", " + words[2] + ")");
                        System.out.println(this.model);
                    }
                }
                else if (words[0].startsWith("r")) {
                    try{
                        this.model = this.model.load(file);
                    } catch (IOException e){};
                    System.out.println("Puzzle reset!");
                    System.out.println(this.model);
                }
                else if (words[0].startsWith("h")){
                    System.out.println("Next step!");
                    this.model.solving();
                    System.out.println(this.model);
                } else if (words[0].startsWith("l")) {
                    try {
                        this.model = this.model.load(words[1]);
                    } catch (IOException e) {
                        System.out.println("Failed to load: " +  words[1]);
                        System.out.println(this.model);
                    }

                } else {
                    displayHelp();
                }
            }
        }
    }
//    public void run() {
//        Scanner in = new Scanner( System.in );
//        for ( ; ; ) {
//            System.out.print( "> " );
//            String line = in.nextLine();
//            String[] words = line.split( "\\s+" );
//            if (words.length > 0) {
//                if (words[0].startsWith("q")) {
//                    break;
//                }
//                else if (words[0].startsWith("s")) {
//                    System.out.println("Selected at (" + words[1] + ", " + words[2] + ")");
//                    if (model.validSelection(Integer.parseInt(words[1]), Integer.parseInt(words[2]))) {
//                        System.out.print( "> " );
//                        String line2 = in.nextLine();
//                        String[] words2 = line2.split("\\s+");
//                        if (words2[0].startsWith("s")) {
//                            model.enterMove(Integer.parseInt(words[1]), Integer.parseInt(words[2]), Integer.parseInt(words2[1]), Integer.parseInt(words2[2]));
//                        }
//                    }
//                }
//                else if (words[0].startsWith("r")) {
//                    model.reset();
//                }
//                else if (words[0].startsWith("h")){
//                    model.solving();
//                }
//
//                else if (words[0].startsWith("l")) {
//                    try {
//                        this.model = new ChessModel(words[1]);
//                    } catch (IOException e) {
//                        System.out.println("Failed to load: " +  words[1]);
//                        System.out.println(this.model);
//                    }
//                } else {
//                    displayHelp();
//                }
//            }
//        }
//    }

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


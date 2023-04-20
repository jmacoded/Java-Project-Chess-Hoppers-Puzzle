package puzzles.chess.solver;

import puzzles.chess.model.ChessConfig;
import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;

public class Chess {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Chess filename");
        } else {
            try {
                ChessConfig init = new ChessConfig(args[0]);
                System.out.println("filename: " + args[0]);

                Solver solve = new Solver(init);
                int step = 0;
                System.out.println(init);
                System.out.println("Total configs: " + solve.getTotalConfigs());
                System.out.println("Unique configs: " + solve.getUniqueConfigs());
                for (Configuration config: solve.getShortestlist()){
                    ChessConfig conf = (ChessConfig) config;
                    System.out.println("Step " + step + ": \n" + conf);
                    step += 1;
                }
                if (solve.getShortestlist().isEmpty()){
                    System.out.println("No Solution");
                }
            } catch (IOException ioe){
                System.out.println("IOException");
            }
        }
    }
}

package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

/**
 * The main program for the strings puzzle
 *
 * @author Teju Rajbabu
 */
public class Strings {
    /**
     * starts up the Strings puzzle application.
     * @param args two String arguments, string1 and string2
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            String str1 = args[0];
            String str2 = args[1];
            Configuration starter = new StringsConfig(str1, str2);
            System.out.println(starter);
            Solver solve = new Solver(starter);
            int step = 0;
            System.out.println("Total configs: " + solve.getTotalConfigs() );
            System.out.println("Unique total configs: " + solve.getUniqueConfigs());
            for (Configuration config: solve.getShortestlist()){
                StringsConfig conf = (StringsConfig) config;
                System.out.println("Step " + step + ": " + conf.getStr1());
                step += 1;
            }
            if (solve.getShortestlist().isEmpty()){
                System.out.println("No Solution");
            }
        }
    }
}

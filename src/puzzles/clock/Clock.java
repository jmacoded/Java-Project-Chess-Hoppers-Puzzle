package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

public class Clock {
    /**
     * starts up the Clock puzzle application.
     * @param args three integer arguments, an hours, start and end
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Clock start stop"));
        } else {
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);
            Configuration starter = new ClockConfig(hours, start, end);
            System.out.println(starter);
            Solver solve = new Solver(starter);
            int step = 0;
            System.out.println("Total configs: " + solve.getTotalConfigs());
            System.out.println("Unique configs: " + solve.getUniqueConfigs());
            for (Configuration config: solve.getShortestlist()){
                ClockConfig conf = (ClockConfig) config;
                System.out.println("Step " + step + ": " + conf.getStart());
                step += 1;
            }
            if (solve.getShortestlist().isEmpty()){
                System.out.println("No Solution");
            }
        }
    }
}

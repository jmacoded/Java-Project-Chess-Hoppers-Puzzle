package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.ArrayList;

public class Hoppers {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        } else {
            try {
                HoppersConfig hoppersConfig = new HoppersConfig(args[0]);
                Solver solver = new Solver(hoppersConfig);
                System.out.println("File: " + args[0]);
                System.out.println(hoppersConfig);
                System.out.println("Total Configs: " + solver.getTotalConfigs());
                System.out.println("Unique Configs: " + solver.getUniqueConfigs());
                ArrayList<Configuration> shortList = solver.getShortestlist();
                if (shortList.isEmpty()) {
                    System.out.println("No Solution");
                } else {
                    int numOfSteps = 0;
                    for (Configuration element : shortList) {
                        System.out.println("Step " + numOfSteps + ":");
                        System.out.println(element.toString());
                        numOfSteps += 1;
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }
    }
}

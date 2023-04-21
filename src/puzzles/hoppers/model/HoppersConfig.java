package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration{
    private static Integer columnDIM;
    private static Integer rowDIM;
    private char[][] grid;

    public HoppersConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] line = in.readLine().split(" ");
            columnDIM = Integer.valueOf(line[0]);
            rowDIM = Integer.valueOf(line[1]);
            this.grid = new char[rowDIM][columnDIM];
            int column = 0;
            int row = 0;
            while (in.ready()) {
                line = in.readLine().split(" ");
                for (String element : line) {
                    this.grid[row][column] = element.charAt(0);
                    row += 1;
                }
                column += 1;
            }
        }
    }

    private HoppersConfig(HoppersConfig copy) {
        this.grid = copy.grid;
    }

    @Override
    public boolean isSolution() {
        for (int column = 0; column < columnDIM; column++) {
            for (int row = 0; row < rowDIM; row++) {
                if (grid[row][column] == 'G') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }
}

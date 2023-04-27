package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

// TODO: implement your HoppersConfig for the common solver

public class HoppersConfig implements Configuration{
    private static Integer columnDIM;
    private static Integer rowDIM;
    private char[][] grid;

    public HoppersConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] line = in.readLine().split("\\s+");
            columnDIM = Integer.valueOf(line[0]);
            rowDIM = Integer.valueOf(line[1]);
            this.grid = new char[rowDIM][columnDIM];
            int column = 0;
            int row = 0;
            while (in.ready()) {
                line = in.readLine().split("\\s+");
                for (String element : line) {
                    this.grid[row][column] = element.charAt(0);
                    row += 1;
                }
                column += 1;
                row = 0;
            }
        }
    }

    protected HoppersConfig(HoppersConfig copy) {
        this.grid = new char[rowDIM][columnDIM];
        for (int r = 0; r < rowDIM; r++) {
            System.arraycopy(copy.grid[r], 0, this.grid[r], 0, columnDIM);
        }
    }

    private Collection<Configuration> isVerticalValid(Character frog, int row, int column) {
        LinkedList<Configuration> verticalNeighbors = new LinkedList<>();
        if (column + 4 < columnDIM && this.grid[row][column + 2] == 'G' && this.grid[row][column + 4] == '.') {
            HoppersConfig hoppersConfig = new HoppersConfig(this);
            hoppersConfig.grid[row][column] = '.';
            hoppersConfig.grid[row][column + 4] = frog;
            hoppersConfig.grid[row][column + 2] = '.';
            verticalNeighbors.add(hoppersConfig);
        }
        if (column - 4 > -1 && this.grid[row][column - 2] == 'G' && this.grid[row][column - 4] == '.') {
            HoppersConfig hoppersConfig = new HoppersConfig(this);
            hoppersConfig.grid[row][column] = '.';
            hoppersConfig.grid[row][column - 4] = frog;
            hoppersConfig.grid[row][column - 2] = '.';
            verticalNeighbors.add(hoppersConfig);
        }
        return verticalNeighbors;
    }

    private Collection<Configuration> isHorizontalValid(Character frog, int row, int column) {
        LinkedList<Configuration> horizontalNeighbors = new LinkedList<>();
        if (row + 4 < rowDIM && this.grid[row + 2][column] == 'G' && this.grid[row + 4][column] == '.') {
            HoppersConfig hoppersConfig = new HoppersConfig(this);
            hoppersConfig.grid[row][column] = '.';
            hoppersConfig.grid[row + 4][column] = frog;
            hoppersConfig.grid[row + 2][column] = '.';
            horizontalNeighbors.add(hoppersConfig);
        }
        if (row - 4 > -1 && this.grid[row - 2][column] == 'G' && this.grid[row - 4][column] == '.') {
            HoppersConfig hoppersConfig = new HoppersConfig(this);
            hoppersConfig.grid[row][column] = '.';
            hoppersConfig.grid[row - 4][column] = frog;
            hoppersConfig.grid[row - 2][column] = '.';
            horizontalNeighbors.add(hoppersConfig);
        }
        return horizontalNeighbors;
    }

    private Collection<Configuration> isNorthWestDiagonalValid(Character frog, int row, int column) {
        LinkedList<Configuration> northWestDiagonalNeighbors = new LinkedList<>();
        if (row + 2 < rowDIM && column + 2 < columnDIM && this.grid[row + 1][column + 1] == 'G' &&
                this.grid[row + 2][column + 2] == '.') {
            HoppersConfig hoppersConfig = new HoppersConfig(this);
            hoppersConfig.grid[row][column] = '.';
            hoppersConfig.grid[row + 2][column + 2] = frog;
            hoppersConfig.grid[row + 1][column + 1] = '.';
            northWestDiagonalNeighbors.add(hoppersConfig);
        }
        if (row - 2 > -1 && column - 2 > -1 && this.grid[row - 1][column - 1] == 'G' &&
                this.grid[row - 2][column - 2] == '.') {
            HoppersConfig hoppersConfig = new HoppersConfig(this);
            hoppersConfig.grid[row][column] = '.';
            hoppersConfig.grid[row - 2][column - 2] = frog;
            hoppersConfig.grid[row - 1][column - 1] = '.';
            northWestDiagonalNeighbors.add(hoppersConfig);
        }
        return northWestDiagonalNeighbors;
    }

    private Collection<Configuration> isNorthEastDiagonalValid(Character frog, int row, int column) {
        LinkedList<Configuration> northEastDiagonalNeighbors = new LinkedList<>();
        if (row + 2 < rowDIM && column - 2 > -1 && this.grid[row + 1][column - 1] == 'G' &&
                this.grid[row + 2][column - 2] == '.') {
            HoppersConfig hoppersConfig = new HoppersConfig(this);
            hoppersConfig.grid[row][column] = '.';
            hoppersConfig.grid[row + 2][column - 2] = frog;
            hoppersConfig.grid[row + 1][column - 1] = '.';
            northEastDiagonalNeighbors.add(hoppersConfig);
        }
        if (row - 2 > -1 && column + 2 < columnDIM && this.grid[row - 1][column + 1] == 'G' &&
                this.grid[row - 2][column + 2] == '.') {
            HoppersConfig hoppersConfig = new HoppersConfig(this);
            hoppersConfig.grid[row][column] = '.';
            hoppersConfig.grid[row - 2][column + 2] = frog;
            hoppersConfig.grid[row - 1][column + 1] = '.';
            northEastDiagonalNeighbors.add(hoppersConfig);
        }
        return northEastDiagonalNeighbors;
    }

    public char[][] getGrid() {
        return this.grid;
    }

    public int getRowDIM(){
        return rowDIM;
    }

    public int getColumnDIM() {
        return columnDIM;
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
        LinkedList<Configuration> neighbors = new LinkedList<>();
        for (int column = 0; column < columnDIM; column++) {
            for (int row = 0; row < rowDIM; row++) {
                if (this.grid[row][column] == 'G' || this.grid[row][column] == 'R') {
                    Character frog = this.grid[row][column];
                    neighbors.addAll(isVerticalValid(frog, row, column));
                    neighbors.addAll(isHorizontalValid(frog, row, column));
                    neighbors.addAll(isNorthWestDiagonalValid(frog, row, column));
                    neighbors.addAll(isNorthEastDiagonalValid(frog, row, column));
                }
            }
        }
        return neighbors;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        boolean firstTime = false;
        for (int c = 0; c < columnDIM; c++) {
            for (int r = 0; r < rowDIM; r++) {
                if (firstTime) {
                    string.append(' ');
                } else {
                    firstTime = true;
                }
                string.append(this.grid[r][c]);
            }
            firstTime = false;
            string.append('\n');
        }
        return string.toString();
    }

    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    public boolean equals(Object other) {
        if (other instanceof Configuration) {
            HoppersConfig otherconfig = (HoppersConfig) other;
            if (Arrays.deepEquals(otherconfig.grid, this.grid)){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}

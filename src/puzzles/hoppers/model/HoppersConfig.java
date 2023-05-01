package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * A hoppers-related class that represents the actual status of the game. Whereabouts of red frog and green frogs,
 * lily-pads, etc. It also contains functions that provides fountains for Solver to receive and attempt to solve with
 * the information
 *
 * @author Jamie Antal
 */
public class HoppersConfig implements Configuration{
    /** An Int representing the range of columns in the game */
    private static Integer columnDIM;
    /** An Int representing the range of rows in the game */
    private static Integer rowDIM;
    /** An Char[][] representing the board and whereabouts of frogs in the game */
    private char[][] grid;

    /**
     * When called, it will attempt to create a new game and board with given filename
     * @param filename Must be String, represents a file to extract information from
     * @throws IOException
     */
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

    /**
     * Simliar to public version, but this protected version is created to copy other HoppersConfig
     * @param copy Must be HoppersConfig, represents a HoppersConfig to copy from
     */
    protected HoppersConfig(HoppersConfig copy) {
        this.grid = new char[rowDIM][columnDIM];
        for (int r = 0; r < rowDIM; r++) {
            System.arraycopy(copy.grid[r], 0, this.grid[r], 0, columnDIM);
        }
    }

    /**
     * Will check the frog at given coordinates of the board to see if it can jump in vertical way. If true, it will
     * create new HopperConfigs and add them to collection and return the collection at the end of function
     * @param frog Must be Char, represents either green frog or red frog
     * @param row Must be Int, represents the where to go to on X axis of the board
     * @param column Must be Int, represents the where to go to on Y axis of the board
     * @return A collection of configurations representing possible steps to solve the puzzle!
     */
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

    /**
     * Will check the frog at given coordinates of the board to see if it can jump in horizontal way. If true, it will
     * create new HopperConfigs and add them to collection and return the collection at the end of function
     * @param frog Must be Char, represents either green frog or red frog
     * @param row Must be Int, represents the where to go to on X axis of the board
     * @param column Must be Int, represents the where to go to on Y axis of the board
     * @return A collection of configurations representing possible steps to solve the puzzle!
     */
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

    /**
     * Will check the frog at given coordinates of the board to see if it can jump in NORTHWEST/SOUTHEAST diagonal way.
     * If true,it will create new HopperConfigs and add them to collection and return the collection at the end of
     * function
     * @param frog Must be Char, represents either green frog or red frog
     * @param row Must be Int, represents the where to go to on X axis of the board
     * @param column Must be Int, represents the where to go to on Y axis of the board
     * @return A collection of configurations representing possible steps to solve the puzzle!
     */
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

    /**
     * Will check the frog at given coordinates of the board to see if it can jump in NORTHEAST/SOUTHWEST diagonal way.
     * If true,it will create new HopperConfigs and add them to collection and return the collection at the end of
     * function
     * @param frog Must be Char, represents either green frog or red frog
     * @param row Must be Int, represents the where to go to on X axis of the board
     * @param column Must be Int, represents the where to go to on Y axis of the board
     * @return A collection of configurations representing possible steps to solve the puzzle!
     */
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

    /**
     * Gets the grid of HopperConfig and returns it
     * @return A Char[][] representing the current status of the board in game
     */
    public char[][] getGrid() {
        return this.grid;
    }

    /**
     * Gets the romDIM of HopperConfig and returns it
     * @return A Int representing the range of rows in game
     */
    public int getRowDIM(){
        return rowDIM;
    }

    /**
     * Gets the columnDIM of HopperConfig and returns it
     * @return A Int representing the range of columns in game
     */
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

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    @Override
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

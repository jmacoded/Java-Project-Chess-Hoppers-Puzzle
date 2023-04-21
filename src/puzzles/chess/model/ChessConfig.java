package puzzles.chess.model;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

// TODO: implement your ChessConfig for the common solver

public class ChessConfig implements Configuration {
    private static int columnDIM;
    private static int rowDIM;
    private char[][] grid;

    /**
     * An empty cell
     */
    char EMPTY = '.';

    /**
     * A cell occupied with a bishop
     */

    private static final Character[] pieces =
            {'B', 'K', 'N', 'P', 'Q', 'R'};
    char BISHOP = 'B';
    char KING = 'K';
    char KNIGHT = 'N';
    char PAWN = 'P';
    char QUEEN = 'Q';
    char ROOK = 'R';


    public ChessConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String line = in.readLine();
            String[] DIMfields = line.split("\\s+");
            rowDIM = Integer.parseInt(DIMfields[0]);
            columnDIM = Integer.parseInt(DIMfields[1]);
            grid = new char[rowDIM][columnDIM];


            int rowtick = 0;
            while (((line = in.readLine()) != null)) {
                String[] rowStr = line.split("\\s+");
                int columntick = 0;
                for (String item : rowStr) {
                    Character obj = item.charAt(0);
                    grid[rowtick][columntick] = obj;
                    columntick += 1;
                }
                rowtick += 1;
            }
        }

    }

    private ChessConfig(ChessConfig other) {
        this.grid = new char[rowDIM][columnDIM];
        for (int r = 0; r < rowDIM; r++) {
            System.arraycopy(other.grid[r], 0, this.grid[r], 0, columnDIM);
        }
    }

    @Override
    public boolean isSolution() {
        int counter = 0;
        for (int r = 0; r < rowDIM; r++) {
            for (int c = 0; c < columnDIM; c++) {
                Character cell = getCell(r, c);
                if (cellCheck(cell)) {
                    counter += 1;
                }
            }
        }
        return counter == 1;
    }

    public char getCell(int row, int col) {
        return grid[row][col];
    }


    @Override
    public Collection<Configuration> getNeighbors() {
        // different possible moves
        LinkedList<Configuration> successors = new LinkedList<>();
        if (isValid(this.grid)){

        }
        for (int r = 0; r < rowDIM; r++) {
            for (int c = 0; c < columnDIM; c++) {
                Character cell = getCell(r, c);
                if (cell.equals(BISHOP)) {
                    ChessConfig BishopNE = NE(r, c, BISHOP);
                    if (BishopNE != null) {
                        successors.add(BishopNE);
                    }
                    ChessConfig BishopNW = NW(r, c, BISHOP);
                    if (BishopNW != null) {
                        successors.add(BishopNW);
                    }
                    ChessConfig BishopSE = SE(r, c, BISHOP);
                    if (BishopSE != null) {
                        successors.add(BishopSE);
                    }
                    ChessConfig BishopSW = SW(r, c, BISHOP);
                    if (BishopSW != null) {
                        successors.add(BishopSW);
                    }
                }
                if (cell.equals(ROOK)) {
                    ChessConfig RookUp = VerticalUp(r, c, ROOK);
                    if (RookUp != null) {
                        successors.add(RookUp);
                    }
                    ChessConfig RookDown = VerticalDown(r, c, ROOK);
                    if (RookDown != null) {
                        successors.add(RookDown);
                    }
                    ChessConfig RookLeft = HorizontalLeft(r, c, ROOK);
                    if (RookLeft != null) {
                        successors.add(RookLeft);
                    }
                    ChessConfig RookRight = HorizontalRight(r, c, ROOK);
                    if (RookRight != null) {
                        successors.add(RookRight);
                    }
                }
                if (cell.equals(KNIGHT)) {
                    LinkedList<ChessConfig> HorsieList = Horsie(r, c, KNIGHT);
                    if (!HorsieList.isEmpty()) {
                        for (ChessConfig config : HorsieList) {
                            if (config != null) {
                                successors.add(config);
                            }
                        }
                    }
                }
                if (cell.equals(PAWN)) {
                    ChessConfig NWPawn = NW1(r, c, PAWN);
                    if (NWPawn != null) {
                        successors.add(NWPawn);
                    }
                    ChessConfig NEPawn = NE1(r, c, PAWN);
                    if (NEPawn != null) {
                        successors.add(NEPawn);
                    }
                }
                if (cell.equals(QUEEN)) {
                    ChessConfig QueenUp = VerticalUp(r, c, ROOK);
                    if (QueenUp != null) {
                        successors.add(QueenUp);
                    }
                    ChessConfig QueenDown = VerticalDown(r, c, ROOK);
                    if (QueenDown != null) {
                        successors.add(QueenDown);
                    }
                    ChessConfig QueenLeft = HorizontalLeft(r, c, ROOK);
                    if (QueenLeft != null) {
                        successors.add(QueenLeft);
                    }
                    ChessConfig QueenRight = HorizontalRight(r, c, ROOK);
                    if (QueenRight != null) {
                        successors.add(QueenRight);
                    }

                }
                if (cell.equals(KING)) {
                    ChessConfig KingSW = SW1(r, c, KING);
                    if (KingSW != null) {
                        successors.add(KingSW);
                    }
                    ChessConfig KingSE = SE1(r, c, KING);
                    if (KingSE != null) {
                        successors.add(KingSE);
                    }
                    ChessConfig KingNW = NW1(r, c, KING);
                    if (KingNW != null) {
                        successors.add(KingNW);
                    }
                    ChessConfig KingNE = NE1(r, c, KING);
                    if (KingNE != null) {
                        successors.add(KingNE);
                    }
                    ChessConfig KingE = E1(r, c, KING);
                    if (KingE != null) {
                        successors.add(KingE);
                    }
                    ChessConfig KingW = W1(r, c, KING);
                    if (KingW != null) {
                        successors.add(KingW);
                    }
                    ChessConfig KingS = S1(r, c, KING);
                    if (KingS != null) {
                        successors.add(KingS);
                    }
                    ChessConfig KingN = N1(r, c, KING);
                    if (KingN != null) {
                        successors.add(KingN);
                    }
                }
            }
        }
        return successors;
    }

    private boolean isValid(char[][] grid){

        return false;
    }

    private ChessConfig VerticalUp(int r, int c, char piece) {
        int moveRow = r;
        while (moveRow - 1 >= 0) {
            moveRow -= 1;
            Character cell = getCell(moveRow, c);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[moveRow][c] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig VerticalDown(int r, int c, char piece) {
        int moveRow = r;
        while (moveRow + 1 < rowDIM) {
            moveRow += 1;
            Character cell = getCell(moveRow, c);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[moveRow][c] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig HorizontalLeft(int r, int c, char piece) {
        int moveCol = c;
        while (moveCol - 1 >= 0) {
            moveCol -= 1;
            Character cell = getCell(r, moveCol);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[moveCol][c] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig HorizontalRight(int r, int c, char piece) {
        int moveCol = c;
        while (moveCol + 1 < columnDIM) {
            moveCol += 1;
            Character cell = getCell(r, moveCol);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[moveCol][c] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig NE(int r, int c, char piece) {
        int moveRow = r;
        int moveCol = c;
        while (moveRow - 1 > 0 & moveCol + 1 < columnDIM) {
            moveRow -= 1;
            moveCol += 1;
            Character cell = getCell(moveRow, moveCol);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[moveRow][moveCol] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig NW(int r, int c, char piece) {
        int moveRow = r;
        int moveCol = c;
        while (moveRow + 1 < rowDIM & moveCol - 1 >= 0) {
            moveRow += 1;
            moveCol -= 1;
            Character cell = getCell(moveRow, moveCol);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[moveRow][moveCol] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig SE(int r, int c, char piece) {
        int moveRow = r;
        int moveCol = c;
        while (moveRow - 1 >= 0 & moveCol + 1 < columnDIM) {
            moveRow -= 1;
            moveCol += 1;
            Character cell = getCell(moveRow, moveCol);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[moveRow][moveCol] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig SW(int r, int c, char piece) {
        int moveRow = r;
        int moveCol = c;
        while (moveRow - 1 >= 0 & moveCol - 1 >= 0) {
            moveRow -= 1;
            moveCol -= 1;
            Character cell = getCell(moveRow, moveCol);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[moveRow][moveCol] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private LinkedList<ChessConfig> Horsie(int r, int c, char piece) {
        LinkedList<ChessConfig> HorsieList = new LinkedList<>();
        if (r - 1 >= 0 & c - 2 >= 0) {
            Character cell = getCell(r - 1, c - 2);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r - 1][c - 2] = piece;
                config.grid[r][c] = EMPTY;
                HorsieList.add(config);
            }
        }
        if (r - 2 >= 0 & c - 1 >= 0) {
            Character cell = getCell(r - 2, c - 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r - 2][c - 1] = piece;
                config.grid[r][c] = EMPTY;
                HorsieList.add(config);
            }
        }
        if (r - 1 >= 0 & c + 2 < columnDIM) {
            Character cell = getCell(r - 1, c + 2);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r - 1][c + 2] = piece;
                config.grid[r][c] = EMPTY;
                HorsieList.add(config);
            }
        }
        if (r - 2 >= 0 & c + 1 < columnDIM) {
            Character cell = getCell(r - 2, c + 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r - 2][c + 1] = piece;
                config.grid[r][c] = EMPTY;
                HorsieList.add(config);
            }
        }
        if (r + 1 < rowDIM & c - 2 >= 0) {
            Character cell = getCell(r + 1, c - 2);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r + 1][c - 2] = piece;
                config.grid[r][c] = EMPTY;
                HorsieList.add(config);
            }
        }
        if (r + 2 < rowDIM & c - 1 >= 0) {
            Character cell = getCell(r + 2, c - 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r + 2][c - 1] = piece;
                config.grid[r][c] = EMPTY;
                HorsieList.add(config);
            }
        }
        if (r + 1 < rowDIM & c + 2 < columnDIM) {
            Character cell = getCell(r + 1, c + 2);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r + 1][c + 2] = piece;
                config.grid[r][c] = EMPTY;
                HorsieList.add(config);
            }
        }
        if (r + 2 < rowDIM & c + 1 < columnDIM) {
            Character cell = getCell(r + 2, c + 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r + 2][c + 1] = piece;
                config.grid[r][c] = EMPTY;
                HorsieList.add(config);
            }
        }
        return HorsieList;
    }

    private ChessConfig NW1(int r, int c, Character piece) {
        if (r - 1 >= 0 & c - 1 >= 0) {
            Character cell = getCell(r - 1, c - 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r - 1][c - 1] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig NE1(int r, int c, Character piece) {
        if (r - 1 >= 0 & c + 1 < columnDIM) {
            Character cell = getCell(r - 1, c + 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r - 1][c + 1] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }
    private ChessConfig SW1(int r, int c, Character piece) {
        if (r + 1 < rowDIM & c - 1 >= 0) {
            Character cell = getCell(r + 1, c - 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r + 1][c - 1] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig SE1(int r, int c, Character piece) {
        if (r + 1 < rowDIM & c + 1 < columnDIM) {
            Character cell = getCell(r + 1, c + 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r + 1][c + 1] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig N1(int r, int c, Character piece) {
        if (r - 1 > 0) {
            Character cell = getCell(r - 1, c);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r - 1][c] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }
    private ChessConfig S1(int r, int c, Character piece) {
        if (r + 1 < rowDIM) {
            Character cell = getCell(r + 1, c);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r + 1][c] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }

    private ChessConfig W1(int r, int c, Character piece) {
        if (c - 1 > 0) {
            Character cell = getCell(r, c - 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r][c -1] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }
    private ChessConfig E1(int r, int c, Character piece) {
        if (c + 1 < columnDIM) {
            Character cell = getCell(r, c + 1);
            if (cellCheck(cell)) {
                ChessConfig config = new ChessConfig(this);
                config.grid[r][c + 1] = piece;
                config.grid[r][c] = EMPTY;
                return config;
            }
        }
        return null;
    }



    private boolean cellCheck(Character cell) {
        return (cell.equals(BISHOP) || cell.equals(KING) || cell.equals(KNIGHT) ||
                cell.equals(PAWN) || cell.equals(QUEEN) || cell.equals(ROOK));
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int r = 0; r < rowDIM; r++) {
            for (int c = 0; c < columnDIM; c++) {
                string.append(getCell(r, c));
                string.append(" ");
            }
            string.append("\n");
        }
        return string.toString();
    }
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }
    public boolean equals(Object other) {
        if (other instanceof Configuration) {
            ChessConfig otherconfig = (ChessConfig) other;
            if (Arrays.deepEquals(otherconfig.grid, this.grid)){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}




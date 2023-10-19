package gameplay;

public class Board {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private char[][] board = new char[HEIGHT][WIDTH];
    private boolean boardIsEmpty = true;
    public final char SEPARATOR = '.';

    public void initialize() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = SEPARATOR;
            }
        }
    }

    @Override
    public String toString() {
        String result = "```   ";

        for (char row = 'A'; row < 'A' + WIDTH; row++) {
            result += row + " ";
        }
        result += "\n";

        for (int i = 0; i < HEIGHT; i++) {
            result += (i + 1) + " ";
            if (i < 9) {
                result += " ";
            }

            for (int j = 0; j < WIDTH; j++) {
                result += board[i][j] + " ";
            }
            result += "\n";
        }
        result += "```\n\n";
        return result;
    }

    @Override
    public Board clone() {
        Board clonedBoard = new Board();
        clonedBoard.initialize();

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                clonedBoard.set(i, j, board[i][j]);
            }
        }
        return clonedBoard;
    }

    public void set(int y, int x, char c) {
        board[y][x] = c;
    }

    public char get(int y, int x) {
        return board[y][x];
    }

    public char[][] getBoard() {
        return board;
    }
}

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game {
    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private int playerOneScore;
    private int playerTwoScore;
    private char[][] board = new char[HEIGHT][WIDTH];


    private String playerOneName;
    private String playerTwoName;

    Game() {
        playerOneScore = 0;
        playerTwoScore = 0;
    }

    public String boardAsString() {
        ArrayList<Character> shortLetters = new ArrayList<>();

        shortLetters.add('f');
        shortLetters.add('i');
        shortLetters.add('j');
        shortLetters.add('l');
        shortLetters.add('I');
        shortLetters.add('C');
        shortLetters.add('E');
        shortLetters.add('H');
        shortLetters.add('J');

        String result = ".   ";
        char column = 'a';

        for (char row = 'A'; row < 'A' + WIDTH; row++) {
            result += row + "  ";
            if (shortLetters.contains(row)) {
                result += " ";
            }
        }
        result += "\n";

        for (int i = 0; i < HEIGHT; i++) {
            result += column;
            if (shortLetters.contains(column)) {
                result += " ";
            }


            for (int j = 0; j < WIDTH; j++) {
                if (i == 4 && j == 3) {
                    result += " J ";
                }
                else {
                    result += "  . ";
                }
                result += "|";
            }
            result += "\n";
            column++;
            if (column == 'm') {
                column++;
            }
        }
        return result;
    }

    public String getScore() {
        return playerOneName + " : " + playerOneScore + "\n"
                + playerTwoName + " : " + playerTwoScore;
    }
    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }
}

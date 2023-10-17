import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game {
    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private LetterBag letterBag = new LetterBag();
    private Player playerOne = new Player("");
    private Player playerTwo = new Player("");
    private char[][] board = new char[HEIGHT][WIDTH];

    Game() {
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = '.';
            }
        }
    }

    public String boardAsString() {
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
        result += "```";
        return result;
    }

    public String getScore() {
        return playerOne.getName() + " : " + playerOne.getScore() + "\n"
                + playerTwo.getName() + " : " + playerTwo.getScore();
    }
    public void addPlayer(Player player) {
        // Make sure no empty names get inputted
        if (player.getName().isEmpty()) {
            throw new RuntimeException("No empty names allowed!");
        }

        // Add player to game if game is not full
        if (playerOne.getName().isEmpty()) {
            this.playerOne = player;
        }
        else if (playerTwo.getName().isEmpty()) {
            this.playerTwo = player;
        }
        else {
            throw new RuntimeException("Game already has two players!");
        }

        player.setLetterBag(letterBag);
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void addWord(Move move) {
        for (int i = 0; i < move.getWord().length(); i++) {
            board[move.getY()][move.getX()] = move.getWord().charAt(i);
            move.increment();
        }
    }

    public String showPlayerOneHand() {
        String result = "Your hand: ";

        for (Character c : playerOne.getHand()) {
            result += c + " ";
        }

        return result;
    }
}

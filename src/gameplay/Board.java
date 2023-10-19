package gameplay;

import dictionary.Dictionary;

import java.util.ArrayList;
import java.util.Collection;

public class Board {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private char[][] board = new char[HEIGHT][WIDTH];
    private boolean boardIsEmpty = true;
    public final char SEPARATOR = '.';
    private int tempScore = 0;
    private String scoredWords = "";
    public boolean isConnected = false;

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

    public void checkForIllegalLongerWord(Move move) {
        int x = move.getX();
        int y = move.getY();
        String word = move.getWord();

        if (move.getDirection() == Direction.ACROSS) {
            // If there are letters left of the start of the word, the move is invalid.
            if (move.getX() > 0 && this.get(y, x) != SEPARATOR) {
                throw new RuntimeException("Word invalid!");
            }
            // If there are letters right of the end of the word, the move is invalid.
            if (x + word.length() < HEIGHT - 1 && this.get(y, x + word.length() + 1) != SEPARATOR) {
                throw new RuntimeException("Word invalid!");
            }
        }
        else {
            //Same logic for words going up and down
            if (y > 0 && this.get(y - 1, x) != SEPARATOR) {
                throw new RuntimeException("Word invalid!");
            }
            // If there are letters below the end of the word, the move is invalid.
            if (y + word.length() < HEIGHT - 1 && this.get(y + word.length() + 1, x) != SEPARATOR) {
                throw new RuntimeException("Word invalid!");
            }
        }
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

    public void checkWordsInOtherDirection(Move move) {
        int oldX = move.getX();
        int oldY = move.getX();
        isConnected = false;
        for (int i = 0; i < move.getWord().length(); i++) {
            int x = move.getX();
            int y = move.getY();
            if (this.get(y, x) == SEPARATOR) {
                this.set(y, x, move.getWord().charAt(i));
                if (move.getDirection() == Direction.ACROSS) {
                    checkUpAndDown(y, x, this);
                }
                else {
                    checkLeftAndRight(y, x, this);
                }
            }
            move.increment();
        }
        move.setX(oldX);
        move.setY(oldY);
    }

    private void checkUpAndDown(int y, int x, Board fakeBoard) {
        String word = "";

        // Go to top of word
        while (y > 0 && fakeBoard.get(y, x) != SEPARATOR) {
            y--;
        }
        if (fakeBoard.get(y, x) == SEPARATOR) {
            y++;
        }

        // Read word into string
        while (y < HEIGHT && fakeBoard.get(y, x) != SEPARATOR) {
            word += fakeBoard.get(y, x);
            y++;
        }

        if (word.length() > 1 && !Dictionary.isValidWord(word)) {
            throw new RuntimeException("Invalid word: " + word);
        }
        else if (word.length() > 1) {
            isConnected = true;
            int score = Dictionary.scoreWord(word);
            tempScore += score;
            scoredWords += "Scored word: " + word + " for " + score + " points! \uD83C\uDF89\n";
        }
    }

    private void checkLeftAndRight(int y, int x, Board fakeBoard) {
        String word = "";

        // Go to top of word
        while (x > 0 && fakeBoard.get(y, x) != SEPARATOR) {
            x--;
        }
        if (fakeBoard.get(y, x) == SEPARATOR) {
            x++;
        }

        // Read word into string
        while (x < HEIGHT && fakeBoard.get(y, x) != SEPARATOR) {
            word += fakeBoard.get(y, x);
            x++;
        }

        if (word.length() > 1 && !Dictionary.isValidWord(word)) {
            throw new RuntimeException("Invalid word: " + word);
        }
        else if (word.length() > 1) {
            isConnected = true;
            int score = Dictionary.scoreWord(word);
            tempScore += score;
            scoredWords += "Scored word: " + word + " for " + score + " points! \uD83C\uDF89\n";
        }
    }

    public Collection<Character> getIntersectingLetters(Move move) {
        Collection<Character> intersectedLetters = new ArrayList<>();
        int oldX = move.getX();
        int oldY = move.getX();

        for (int i = 0; i < move.getWord().length(); i++) {
            if (board[move.getY()][move.getX()] != SEPARATOR) {
                if (board[move.getY()][move.getX()] != move.getWord().charAt(i)) {
                    throw new RuntimeException("Invalid word!");
                }
                else {
                    intersectedLetters.add(board[move.getY()][move.getX()]);
                }
            }
            move.increment();
        }
        move.setX(oldX);
        move.setY(oldY);

        return intersectedLetters;
    }

    public int getTempScore() {
        return tempScore;
    }

    public String getScoredWords() {
        return scoredWords;
    }
}

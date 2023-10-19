package gameplay;

import dictionary.Dictionary;
import dictionary.LetterBag;
import dictionary.LetterValues;

import java.util.ArrayList;
import java.util.Collection;

public class Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private Board board2 = new Board();
    private char[][] board = new char[HEIGHT][WIDTH];
    private boolean boardIsEmpty = true;

    private final char SEPARATOR = '.';
    private long owner;
    private LetterBag letterBag = new LetterBag();
    private Player playerOne = new Player("");
    private Player playerTwo = new Player("");
    private boolean isStarted = false;
    private boolean readyToPlay;
    private Turn turn;
    private int tempScore;
    private boolean isConnected;
    private String scoredWords = "";
    private boolean isEnded = false;
    private TurnType lastTurn = TurnType.NONE;


    public Game(long owner) {
        this.owner = owner;
        board2.initialize();
        turn = Turn.ONE;
    }

    public String getScore() {
        return playerOne.getName() + " : " + playerOne.getScore() + "\n"
                + playerTwo.getName() + " : " + playerTwo.getScore() + "\n\n";
    }

    public void addPlayer(Player player) {
        // Make sure no empty names get inputted
        if (player.getName().isEmpty()) {
            throw new RuntimeException("No empty names allowed!");
        }

        // Add player to game if game is not full
        if (playerOne.getName().isEmpty()) {
            this.playerOne = player;
            System.out.println("set player 1 to " + playerOne.getName() + " " + playerOne.getUserID());
        }
        else if (playerTwo.getName().isEmpty()) {
            this.playerTwo = player;
            System.out.println("set player 2 to " + playerTwo.getName() + " " + playerTwo.getUserID());
            readyToPlay = true;
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
            int x = move.getX();
            int y = move.getY();
            if (board2.get(y, x) == board2.SEPARATOR) {
                board2.set(y, x, move.getWord().charAt(i));
            }
            move.increment();
        }
    }


    public boolean isStarted() {
        return isStarted;
    }

    public void setIsStarted(boolean started) {
        isStarted = started;
    }

    public long getOwner() {
        return owner;
    }

    public Turn whoseTurn() {
        return turn;
    }

    public void switchTurn() {
        if (turn == Turn.ONE) {
            turn = Turn.TWO;
        }
        else {
            turn = Turn.ONE;
        }
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    public String showPlayerHand(Player player) {
        String result = "Your hand: ";

        for (Character c : player.getHand()) {
            result += c + " ";
        }

        return result + "\n\n";
    }

    public String showPlayerHandWithLetterValues(Player player) {
        String result = "Your letter values: ";

        for (Character c : player.getHand()) {
            result += c + "(" + LetterValues.get(c) + ") ";
        }

        return result + "\n\n";
    }

    public LetterBag getLetterBag() {
        return letterBag;
    }

    public Collection<Character> getIntersectingLetters(Move move) {
        Collection<Character> intersectedLetters = new ArrayList<>();
        int oldX = move.getX();
        int oldY = move.getY();
        isConnected = false;
        tempScore = 0;

        Board fakeBoard = board2.clone();

        checkForIllegalLongerWord(move);

        for (int i = 0; i < move.getWord().length(); i++) {
            int x = move.getX();
            int y = move.getY();
            if (fakeBoard.get(y, x) != SEPARATOR) {
                if (fakeBoard.get(y, x) != move.getWord().charAt(i)) {
                    throw new RuntimeException("Invalid word!");
                }
                else {
                    intersectedLetters.add(fakeBoard.get(y, x));
                }
            }
            else {
                fakeBoard.set(y, x, move.getWord().charAt(i));
                checkWordsInOtherDirection(move, fakeBoard.getBoard());
            }
            move.increment();
        }

        move.setX(oldX);
        move.setY(oldY);

        // This means that the entire word is already on the board, and the move is invalid
        if (intersectedLetters.size() == move.getWord().length()) {
            throw new RuntimeException("Word already on board!");
        }

        if (isConnected == false && intersectedLetters.isEmpty() && !boardIsEmpty) {
            throw new RuntimeException("Word must be connected!");
        }

        else if (boardIsEmpty && firstMoveInvalid(move)) {
            throw new RuntimeException("First move must intersect H8 square!");
        }

        scoredWords += "Scored word: " + move.getWord() + " for "
                + Dictionary.scoreWord(move.getWord()) + " points! \uD83C\uDF89\n";

        return intersectedLetters;
    }

    private boolean firstMoveInvalid(Move move) {
        int oldX = move.getX();
        int oldY = move.getY();

        for (int i = 0; i < move.getWord().length(); i++) {
            if (move.getX() == HEIGHT / 2 && move.getY() == WIDTH / 2) {
                move.setX(oldX);
                move.setY(oldY);
                return false;
            }
            move.increment();
        }

        move.setX(oldX);
        move.setY(oldY);
        return true;
    }


    private void checkForIllegalLongerWord(Move move) {
        board = board2.getBoard();
        if (move.getDirection() == Direction.ACROSS) {
            // If there are letters left of the start of the word, the move is invalid.
            if (move.getX() > 0 && board[move.getY()][move.getX() - 1] != SEPARATOR) {
                throw new RuntimeException("Word invalid!");
            }
            // If there are letters right of the end of the word, the move is invalid.
            if (move.getX() + move.getWord().length() < HEIGHT - 1 &&
                    board[move.getY()][move.getX() + move.getWord().length() + 1] != SEPARATOR)
            {
                throw new RuntimeException("Word invalid!");
            }
        }
        else {
            //Same logic for words going up and down
            if (move.getY() > 0 && board[move.getY() - 1][move.getX()] != SEPARATOR) {
                throw new RuntimeException("Word invalid!");
            }
            // If there are letters below the end of the word, the move is invalid.
            if (move.getY() + move.getWord().length() < HEIGHT - 1 &&
                    board[move.getY() + move.getWord().length() + 1][move.getX()] != SEPARATOR)
            {
                throw new RuntimeException("Word invalid!");
            }
        }

    }

    private void checkWordsInOtherDirection(Move move, char[][] fakeBoard) {
        if (move.getDirection() == Direction.ACROSS) {
            checkUpAndDown(move, fakeBoard);
        }
        else {
            checkLeftAndRight(move, fakeBoard);
        }
    }

    private void checkUpAndDown(Move move, char[][] fakeBoard) {
        int y = move.getY();
        int x = move.getX();
        String word = "";

        // Go to top of word
        while (y > 0 && fakeBoard[y][x] != SEPARATOR) {
            y--;
        }
        if (fakeBoard[y][x] == SEPARATOR) {
            y++;
        }

        // Read word into string
        while (y < HEIGHT && fakeBoard[y][x] != SEPARATOR) {
            word += fakeBoard[y][x];
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

    private void checkLeftAndRight(Move move, char[][] fakeBoard) {
        int y = move.getY();
        int x = move.getX();
        String word = "";

        // Go to top of word
        while (x > 0 && fakeBoard[y][x] != SEPARATOR) {
            x--;
        }
        if (fakeBoard[y][x] == SEPARATOR) {
            x++;
        }

        // Read word into string
        while (x < HEIGHT && fakeBoard[y][x] != SEPARATOR) {
            word += fakeBoard[y][x];
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

    public int getTempScore() {
        return tempScore;
    }

    public void setBoardIsEmpty(boolean boardIsEmpty) {
        this.boardIsEmpty = boardIsEmpty;
    }

    public String getScoredWords() {
        return scoredWords + "\n";
    }

    public void clearScoredWords() {
        scoredWords = "";
    }

    public void endGame() {
        isEnded = true;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setLastTurn(TurnType lastTurn) {
        this.lastTurn = lastTurn;
    }

    public TurnType getLastTurn() {
        return lastTurn;
    }

    public String boardAsString() {
        return board2.toString();
    }
}

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
        intersectedLetters = fakeBoard.getIntersectingLetters(move);
        System.out.println(intersectedLetters);

        fakeBoard = board2.clone();
        fakeBoard.checkForIllegalLongerWord(move);
        fakeBoard.checkWordsInOtherDirection(move);

        tempScore = fakeBoard.getTempScore();
        scoredWords = fakeBoard.getScoredWords();
        isConnected = fakeBoard.isConnected;

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

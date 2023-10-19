package gameplay;

import dictionary.Dictionary;
import dictionary.LetterBag;
import java.util.ArrayList;
import java.util.List;

public class User {
    private static final String EXCLAMATION = "❗";
    private final long userId;
    private Game game;
    private Turn myTurn;
    private Player myPlayer;
    private boolean readyToNotify = false;
    public boolean sendGameOverMessage = false;

    String response = "";
    private Status status = Status.UNINITIALIZED;


    public User(long userId) {
        this.userId = userId;
        this.game = new Game(userId);
    }

    public long getId() {
        return userId;
    }


    public void resetResponse() {
        response = "";
    }

    public String getResponse() {
        return response;
    }

    public void handleCommand(String messageReceived, long userId) {
        if (game.isEnded()) {
            response += "Game was ended... Enter start or join for a new one!";
            status = Status.UNINITIALIZED;
            return;
        }

        switch (status) {
            case UNINITIALIZED -> handleInitialization(messageReceived);
            case SEARCHING -> findGame(messageReceived);
            case GET_P1_NAME -> addPlayerOne(messageReceived, userId);
            case GET_P2_NAME -> addPlayerTwo(messageReceived, userId);
            case GET_ID -> addIdToGame(messageReceived);
            case JOINED -> notMyTurn();
            case TAKE_TURN -> playTurn(messageReceived);
            case SWAP -> swap(messageReceived);
            case PASS -> pass();
            case PLAY_WORD -> playWord(messageReceived);
            default -> addDefaultMessage();
        }
    }

    private void swap(String messageReceived) {
        char charArray[] = messageReceived.toUpperCase().toCharArray();
        List<Character> handCopy = new ArrayList<>();
        List<Character> removedLetters = new ArrayList<>();
        LetterBag letterBag = game.getLetterBag();

        handCopy.addAll(myPlayer.getHand());

        for (Character c : charArray) {
            if (!handCopy.contains(c)) {
                response += "Swap failed, some letters weren't found\n\n";
                status = Status.TAKE_TURN;
                return;
            }
            removedLetters.add(c);
            handCopy.remove(c);
        }
        myPlayer.setHand(handCopy);
        myPlayer.addToHand(7 - handCopy.size());

        letterBag.addLetters(removedLetters);

        response += "Swap successful! \n\n";
        showGameState();
        endTurn();
        game.setLastTurn(TurnType.SWAP);
    }

    private void pass() {
        System.out.println("Passing turn");
        endTurn();
        if (game.getLastTurn() == TurnType.PASS) {
            game.endGame();
            sendGameOverMessage = true;
        }
        game.setLastTurn(TurnType.PASS);
    }

    private void addDefaultMessage() {
        response += "Couldn't parse your command... we're working on it\n\n";
        showGameState();
    }

    private void playTurn(String messageReceived) {
        if (notMyTurn() && !messageReceived.toUpperCase().equals("VALUES")) {
            return;
        }

        switch (messageReceived.toUpperCase()) {
            case "SWAP" -> status = Status.SWAP;
            case "PASS" -> status = Status.PASS;
            case "PLAY" -> status = Status.PLAY_WORD;
            case "VALUES" -> status = Status.SHOW_LETTERS;
            case "EXIT" -> status = Status.EXITED;
        }
        addPlayPrompt(status);
    }

    private void addPlayPrompt(Status status) {
        switch (status) {
            case SWAP -> response += "Enter tiles to swap, with no separation, like this: ABCD";
            case PLAY_WORD -> response += "Enter your word and its coordinates / direction (across or down), like this: Word h6 down";
            case PASS -> pass();
            case SHOW_LETTERS -> showLetters();
            case EXITED -> exitGame();
        }
    }

    private void exitGame() {
        game.endGame();
        response += "Game ended successfully. Enter start or join for new game: ";
        status = Status.UNINITIALIZED;
        readyToNotify = true;
    }

    private void showLetters() {
        response += game.showPlayerHandWithLetterValues(myPlayer);
        status = Status.TAKE_TURN;
    }


    private boolean notMyTurn() {
        if (waitingForPlayer()) {
            response += "Waiting for other player to join...\n";
            return true;
        } else if (!isMyTurn()) {
            response += "Not your turn! Wait for other player.\n\n";
            return true;
        }
        return false;
    }

    private void addIdToGame(String messageReceived) {
        if (GameLibrary.containsGame(messageReceived)) {
            response += "Already taken :(";
            status = Status.UNINITIALIZED;
        } else {
            GameLibrary.add(messageReceived, game);
            response += "Game created with passcode: " + messageReceived + "\n\n";
            response += "We will notify you when your opponent joins.";
            status = Status.TAKE_TURN;
        }
    }

    private void addPlayerTwo(String messageReceived, long userId) {
        myPlayer = new Player(messageReceived);
        game.addPlayer(myPlayer);
        myPlayer.addToHand(7);
        myPlayer.setUserID(userId);
        response += "You were added to the game!";
        status = Status.JOINED;
        readyToNotify = true;
        myTurn = Turn.TWO;
    }

    private void addPlayerOne(String messageReceived, long userId) {
        myPlayer = new Player(messageReceived);
        game.addPlayer(myPlayer);
        myPlayer.addToHand(7);
        myPlayer.setUserID(userId);
        response += "You were added to the game! Enter a passcode for your game: ";
        myTurn = Turn.ONE;
        status = Status.GET_ID;
    }

    private void findGame(String messageReceived) {
        if (GameLibrary.containsGame(messageReceived)) {
            game = GameLibrary.get(messageReceived);
            response += "Found your game! Enter your name: \n";
            status = Status.GET_P2_NAME;
        }
        else {
            response += "Couldn't find a game with that id... Type start or join: ";
            status = Status.UNINITIALIZED;
        }
    }

    private void handleInitialization(String messageReceived) {
        if (messageReceived.toUpperCase().equals("START")) {
            response += "Game created!\nPlease enter your name: ";
            status = Status.GET_P1_NAME;
        } else if (messageReceived.toUpperCase().equals("JOIN")) {
            response += "Enter unique game ID: ";
            this.status = Status.SEARCHING;
        } else {
            response += "Invalid command, enter Start or Join";
        }
    }

    private boolean waitingForPlayer() {
        if (game.isReady()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isMyTurn() {
        return game.whoseTurn() == myTurn;
    }

    public void playWord(String messageReceived) {
        try {
            Move move = MoveParser.parseMove(messageReceived);
            checkIfWordIsPlayable(move);
            game.addWord(move);
            myPlayer.makeMove(move);
            myPlayer.addScore(game.getTempScore());
            game.setBoardIsEmpty(false);
            response += game.getScoredWords();
            showGameState();
            endTurn();
            game.setLastTurn(TurnType.PLAY);
            game.clearScoredWords();
        } catch (ArrayIndexOutOfBoundsException e) {
            game.clearScoredWords();
            response += EXCLAMATION + "Out of bounds..." + EXCLAMATION + "\n\n";
            showGameState();
            status = Status.TAKE_TURN;
        } catch (Exception e) {
            game.clearScoredWords();
            response += EXCLAMATION + e.getMessage() + EXCLAMATION + "\n\n";
            showGameState();
            status = Status.TAKE_TURN;
        }
    }

    private void endTurn() {
        game.switchTurn();
        readyToNotify = true;
        status = Status.TAKE_TURN;
        response += "Turn is over, wait for notification from opponent\n\n";
    }

    private void checkIfWordIsPlayable(Move move) {
        List<Character> handLetters = new ArrayList<>();
        List<Character> wordLetters = new ArrayList<>();

        if (Dictionary.isValidWord(move.getWord()) == false) {
            throw new RuntimeException("Word invalid: " + move.getWord());
        }

        handLetters.addAll(myPlayer.getHand());
        handLetters.addAll(game.getIntersectingLetters(move));

        for (int i = 0; i < move.getWord().length(); i++) {
            wordLetters.add(move.getWord().charAt(i));
        }

        for (Character c : wordLetters) {
            if (!handLetters.contains(c)) {
                throw new RuntimeException("You don't have the right letters for that!");
            } else {
                handLetters.remove(c);
            }
        }
    }

    public void showGameState() {
        response += game.getScore();
        response += game.boardAsString();
        response += game.showPlayerHand(myPlayer);
    }

    public String getGameState() {
        return game.getScore()
                + game.boardAsString()
                + game.showPlayerHand(myPlayer);
    }

    public boolean isReadyToNotify() {
        return readyToNotify;
    }

    public void wasNotified() {
        readyToNotify = false;
    }

    public long getOpponentId() {
        if (game.getPlayerOne() == myPlayer) {
            return game.getPlayerTwo().getUserID();
        } else {
            return game.getPlayerOne().getUserID();
        }
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public gameplay.Game getGame() {
        return game;
    }

    public void welcome() {
        response += "Welcome to scrabble bot!\n";
    }
}

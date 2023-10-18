package gameplay;

import dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final long chatId;
    private final long userId;
    private Game game;
    String response = "";
    private Status status = Status.UNINITIALIZED;


    public User(long userId, long chatId) {
        this.userId = userId;
        this.chatId = chatId;
        this.game = new Game(userId);
    }

    public long getId() {
        return userId;
    }

    public void welcome() {
        response += "Welcome to the game! Please enter Start or Join";
    }

    public void resetResponse() {
        response = "";
    }

    public String getResponse() {
        return response;
    }

    // Todo change this into a switch statement based on gameplay.Status.
    public void handleCommand(String messageReceived, long userId) {
        if (status == Status.UNINITIALIZED && messageReceived.toUpperCase().equals("START")) {
            response += "Game created!\nPlease enter your name: ";
            status = Status.GET_P1_NAME;
        }
        else if (status == Status.UNINITIALIZED && messageReceived.toUpperCase().equals("JOIN")) {
            response += "Enter unique game ID: ";
            this.status = Status.SEARCHING;
        }
        else if (status == Status.SEARCHING) {
            if (GameLibrary.containsGame(messageReceived)) {
                response += "Found your game! Enter your name: \n";
                status = Status.GET_P2_NAME;
            }
            else if (messageReceived.equals("EXIT")) {
                status = Status.UNINITIALIZED;
            }
            else {
                response += "Couldn't find a game with that id... Try again or type EXIT to start over: ";
            }
        }
        else if (status == Status.GET_P1_NAME){
            // Todo add empty string check here.
            Player player = new Player(messageReceived);
            game.addPlayer(player);
            player.addToHand(7);
            response += "You were added to the game! Enter a passcode for your game: ";

            status = Status.GET_ID;
        }
        else if (status == Status.GET_P2_NAME) {
            // Todo add empty string check here.
            Player player = new Player(messageReceived);
            game.addPlayer(player);
            player.addToHand(7);
            response += "You were added to the game!";
            status = Status.PLAYING;
        }
        else if (status == Status.GET_ID) {
            if (GameLibrary.containsGame(messageReceived)) {
                response += "Already taken :( try again! Enter your passcode: ";
            }
            else {
                GameLibrary.add(messageReceived, game);
                response += "Game created with passcode: " + messageReceived + "\n\n";
                showGameState();
                status = Status.PLAYING;
            }
        }
        else if (status == Status.PLAYING) {
            takeTurn(messageReceived);
        }
        else {
            response += "Word format wrong or not recognized! Enter in format: WORD H5 DOWN\n\n";
            showGameState();
        }
    }

    public void takeTurn(String messageReceived) {
        Move move = MoveParser.parseMove(messageReceived);
        if (Dictionary.isValidWord(move.getWord()) == false) {
            response += "Invalid word! " + move.getWord();
        }
        else if (wordNotPossible(move)) {
            response += "Move not possible with your letters! " + move.getWord() + "\n\n";
        }
        else {
            game.addWord(move);
            game.getPlayerOne().makeMove(move);
        }

        showGameState();
    }

    private boolean wordNotPossible(Move move) {
        List<Character> handLetters = new ArrayList<>();
        List<Character> wordLetters = new ArrayList<>();

        handLetters.addAll(game.getPlayerOne().getHand());

        for (int i = 0; i < move.getWord().length(); i++) {
            wordLetters.add(move.getWord().charAt(i));
        }

        for (Character c : wordLetters) {
            if (!handLetters.contains(c)) {
                return true;
            }
            else {
                handLetters.remove(c);
            }
        }
        return false;
    }

    public void showGameState() {
        response += game.getScore();
        response += game.boardAsString();
        response += game.showPlayerOneHand();
    }
}

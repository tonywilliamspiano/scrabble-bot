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
            response += "gameplay.Game created with ID: TEST\nPlease enter your name: ";
            status = Status.GET_NAME;
        }
        else if (status == Status.UNINITIALIZED && messageReceived.toUpperCase().equals("JOIN")) {
            response += "Enter unique game ID: ";
            this.status = Status.SEARCHING;
        }
        else if (status == Status.SEARCHING) {
            response += "Found your game!\n";
            showGameState();
        }
        else if (status == Status.GET_NAME){
            Player player = new Player(messageReceived);
            game.addPlayer(player);
            player.addToHand(7);
            response += "You were added to the game!";

            // Placeholder!
            game.addPlayer(new Player("Max Mustermann"));

            status = Status.PLAYING;
            showGameState();
        }
        else if (status == Status.PLAYING) {
            takeTurn(messageReceived);
        }
        else {
            response += "Word format wrong or not recognized! Enter in format: WORD H5 DOWN\n\n";
            showGameState();
        }
    }

    private boolean isValidGameId(String messageReceived) {
        return false;
    }

    public void takeTurn(String messageReceived) {
        Move move = MoveParser.parseMove(messageReceived);
        if (Dictionary.isValidWord(move.getWord()) == false) {
            response += "Invalid word! " + move.getWord();
        }
        else if (wordNotPossible(move)) {
            response += "gameplay.Move not possible with your letters! " + move.getWord() + "\n\n";
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

package gameplay;

import dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class User {
	private final long userId;
	private Game game;
	private Turn myTurn;
	private Player myPlayer;
	private boolean readyToNotify = false;

	String response = "";
	private Status status = Status.UNINITIALIZED;


	public User(long userId) {
		this.userId = userId;
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
		switch (status) {
			case UNINITIALIZED -> handleInitialization(messageReceived);
			case SEARCHING -> findGame(messageReceived);
			case GET_P1_NAME -> addPlayerOne(messageReceived, userId);
			case GET_P2_NAME -> addPlayerTwo(messageReceived, userId);
			case GET_ID -> addIdToGame(messageReceived);
            case TAKE_TURN -> playTurn(messageReceived);
            case SWAP -> swap(messageReceived);
            case PASS -> pass(messageReceived);
            case PLAY_WORD -> playWord(messageReceived);
            default -> addDefaultMessage();
        }
	}

    private void swap(String messageReceived) {
    }

    private void playWord(String messageReceived) {
        
    }

    private void pass(String messageReceived) {
        
    }

    private void addDefaultMessage() {
        response += "Couldn't parse your command... we're working on it\n\n";
        showGameState();
    }

    private void playTurn(String messageReceived) {
//        if (waitingForPlayer()) {
//            response += "Waiting for other player to join...\n";
//        }
//        else if  (isMyTurn()) {
                takeTurn(messageReceived);
//        }
//        else {
//            response += "Not your turn! Wait for other player.\n\n";
//            showGameState();
//        }
    }

    private void addIdToGame(String messageReceived) {
        if (GameLibrary.containsGame(messageReceived)) {
            response += "Already taken :( try again! Enter your passcode: ";
        } else {
            GameLibrary.add(messageReceived, game);
            response += "Game created with passcode: " + messageReceived + "\n\n";
            showGameState();
            status = Status.TAKE_TURN;
        }
    }

    private void addPlayerTwo(String messageReceived, long userId) {
		// Todo add empty string check here.
		myPlayer = new Player(messageReceived);
		game.addPlayer(myPlayer);
		myPlayer.addToHand(7);
		myPlayer.setUserID(userId);
		response += "You were added to the game!";
		status = Status.TAKE_TURN;
		myTurn = Turn.TWO;
	}

	private void addPlayerOne(String messageReceived, long userId) {
		// Todo add empty string check here.
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
		} else if (messageReceived.equals("EXIT")) {
			status = Status.UNINITIALIZED;
		} else {
			response += "Couldn't find a game with that id... Try again or type EXIT to start over: ";
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
		System.out.println(game.isReady());
		if (game.isReady()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isMyTurn() {
		return game.whoseTurn() == myTurn;
	}

	public void takeTurn(String messageReceived) {
		Move move = MoveParser.parseMove(messageReceived);
		if (Dictionary.isValidWord(move.getWord()) == false) {
			response += "Invalid word! " + move.getWord() + "\n\n";
		} else if (wordNotPossible(move)) {
			response += "Move not possible with your letters! " + move.getWord() + "\n\n";
		} else {
			game.addWord(move);
			myPlayer.makeMove(move);
			game.switchTurn();
			readyToNotify = true;
		}

		showGameState();
	}

	private void addPrompt() {
		response += "Enter **WORD**, **PASS** or **SWAP**";
	}

	private boolean wordNotPossible(Move move) {
		List<Character> handLetters = new ArrayList<>();
		List<Character> wordLetters = new ArrayList<>();

		handLetters.addAll(myPlayer.getHand());

		for (int i = 0; i < move.getWord().length(); i++) {
			wordLetters.add(move.getWord().charAt(i));
		}

		for (Character c : wordLetters) {
			if (!handLetters.contains(c)) {
				return true;
			} else {
				handLetters.remove(c);
			}
		}
		return false;
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
}

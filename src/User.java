public class User {
    private final long chatId;
    private final long userId;
    private Game game = new Game();
    String response = "";
    private Status status = Status.UNINITIALIZED;


    public User(long userId, long chatId) {
        this.userId = userId;
        this.chatId = chatId;
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

    public void parseCommand(String messageReceived, long userId) {
        if (status == Status.UNINITIALIZED && messageReceived.toUpperCase().equals("START")) {
            game = new Game();
            game.setOwner(userId);
            response += "Game created with ID: TEST\nPlease enter your name: ";
            status = Status.GET_NAME;
        }
        else if (status == Status.UNINITIALIZED && messageReceived.toUpperCase().equals("JOIN")) {
            response += "Enter unique game ID: ";
            this.status = Status.SEARCHING;
        }
        else if (status == Status.SEARCHING) {
            response += "Searching for game...";
        }
        else if (status == Status.GET_NAME){
            game.addPlayer(new Player(messageReceived));
            response += "You were added to the game!";

            // Placeholder!
            game.addPlayer(new Player("Max Mustermann"));

            status = Status.PLAYING;
        }
        else if (status == Status.PLAYING) {
            takeTurn(messageReceived);
        }
    }

    private boolean isValidGameId(String messageReceived) {
        return false;
    }

    public void takeTurn(String messageReceived) {
        Move move = MoveParser.parseMove(messageReceived);
        game.addWord(move);
        game.getPlayerOne().makeMove(move);

        response += game.getScore();
        response += game.boardAsString();
        response += game.showPlayerOneHand();
    }
}

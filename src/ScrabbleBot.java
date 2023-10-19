import dictionary.Dictionary;
import gameplay.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import secret.Token;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ScrabbleBot extends TelegramLongPollingBot {
    List<User> users = new ArrayList<>();
    Dictionary dictionary;
    long userId;
    String messageReceived;

    ScrabbleBot() throws FileNotFoundException {
        this.dictionary = new Dictionary();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            getMessageAndId(update);
            User user = generateUser();
            sendResponsesToUser(user);
        } catch (Exception e) {
            sendResponse(userId, "Something went wrong in the backend :( Try again later");
            System.out.println("Exception caught! " + e.getMessage());
        }
    }

    private User generateUser() {
        int userIndex = userExists(userId);
        User user;

        // If user exists, let him process the request
        if (userIndex >= 0) {
            user = users.get(userIndex);
            user.resetResponse();
            user.handleCommand(messageReceived, userId);
        } else {
            // create a new user
            user = new User(userId);
            users.add(user);
            user.welcome();
        }
        return user;
    }
    private void sendResponsesToUser(User user) {
        sendResponse(userId, user.getResponse());
        sendNotificationIfNecessary(user);

        if (user.getStatus() == Status.TAKE_TURN && user.isMyTurn()
                && user.getGame().isReady()) {
            sendMenu(userId);
        } else if (user.getStatus() == Status.UNINITIALIZED){
            sendTitleMenu(user.getId());
        }
    }

    private void getMessageAndId(Update update) {
        if (update.hasMessage()) {
            messageReceived = update.getMessage().getText();
            userId = update.getMessage().getFrom().getId();
        }
        else if (update.hasCallbackQuery()) {
            messageReceived = update.getCallbackQuery().getData();
            userId = update.getCallbackQuery().getMessage().getChatId();
        }
    }

    private void sendNotificationIfNecessary(User user) {
        if (!user.isReadyToNotify()) {
            return;
        }
        else {
            sendNotificationToUser(user);
        }
    }

    private void sendNotificationToUser(User user) {
        // Find the user's opponent to send him/her a notification
        int userIndex = userExists(user.getOpponentId());
        User opponent = users.get(userIndex);

        String response = "";
        if (user.getStatus() == Status.JOINED) {
            tellOpponentThatUserJoined(user, opponent);
        } else if (user.sendGameOverMessage == true) {
            endAndRestartGame(user, response, opponent);
        } else if (user.getGame().isEnded()) {
            tellOpponentThatUserExited(opponent);
        } else {
            notifyOpponentOfHisTurn(opponent);
        }
        user.wasNotified();
    }

    private void tellOpponentThatUserJoined(User user, User opponent) {
        String response;
        response = "Other player has joined! Your turn: \n\n";
        user.setStatus(Status.TAKE_TURN);
        response += opponent.getGameState();
        sendResponse(opponent.getId(), response);
        sendMenu(opponent.getId());
    }

    private void notifyOpponentOfHisTurn(User opponent) {
        String response;
        response = "Other player has played! Your turn: \n\n";
        response += opponent.getGameState();
        sendLetterKeyboard(userId);
        sendMenu(opponent.getId());
    }

    private void tellOpponentThatUserExited(User opponent) {
        System.out.println("Sending notification to opponent" + opponent.getId());
        sendResponse(opponent.getId(), "Game was ended by opponent... They must be scared to lose!");
        opponent.setStatus(Status.UNINITIALIZED);
        sendTitleMenu(opponent.getId());
    }

    private void endAndRestartGame(User user, String response, User opponent) {
        response += gameOverMessage(user);
        users.remove(user);
        user.resetResponse();
        sendResponse(user.getId(), response);

        users.remove(opponent);
        sendResponse(opponent.getId(), response);
    }

    private String gameOverMessage(User user) {
        // Find game and players
        Game game = user.getGame();
        Player p1 = user.getGame().getPlayerOne();
        Player p2 = user.getGame().getPlayerTwo();
        String result = "Game over, the final score is... \n\n";

        // Add score
        result += game.getScore() + "\n";

        // determine winner of game with party emojis
        if (p1.getScore() > p2.getScore()) {
            result += p1.getName() + " wins! \uD83C\uDF89\uD83C\uDF89\uD83C\uDF89";
        } else if (p2.getScore() > p1.getScore()) {
            result += p2.getName() + " wins! \uD83C\uDF89\uD83C\uDF89\uD83C\uDF89";
        } else {
            result += "It's a tie, you both did great!";
        }
        return result;
    }

    private int userExists(long userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String getBotUsername() {
        return "ScrabbleBot";
    }

    @Override
    public String getBotToken() {
        return Token.TOKEN;
    }

    private void sendResponse(long chatId, String s) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(s);
        msg.setParseMode("Markdown");

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void sendMenu(long userId) {
        SendMessage message = new SendMessage();

        message.setText("Enter play, swap, or pass:");
        message.setChatId(userId);
        message.setReplyMarkup(KeyboardFactory.getMenuKeyboard());
        message.setParseMode("Markdown");
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendTitleMenu(long userId) {
        SendMessage message = new SendMessage();

        message.setText("Enter start or join: ");
        message.setChatId(userId);
        message.setReplyMarkup(KeyboardFactory.getFirstKeyboard());
        message.setParseMode("Markdown");
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLetterKeyboard(long userId) {
        SendMessage message = new SendMessage();

        message.setText("You can still see letter values, if you want:");
        message.setChatId(userId);
        message.setReplyMarkup(KeyboardFactory.getLetterKeyboard());
        message.setParseMode("Markdown");
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

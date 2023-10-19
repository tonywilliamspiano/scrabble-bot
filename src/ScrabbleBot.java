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
    long chatId;

    ScrabbleBot() throws FileNotFoundException {
        this.dictionary = new Dictionary();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                messageReceived = update.getMessage().getText();
                userId = update.getMessage().getFrom().getId();
            }
            else if (update.hasCallbackQuery()) {
                messageReceived = update.getCallbackQuery().getData();
                userId = update.getCallbackQuery().getMessage().getChatId();
            }
            else {
                return;
            }

            int userIndex = userExists(userId);
            User user;
            // If user exists, let him process the request
            if (userIndex >= 0) {
                user = users.get(userIndex);
                user.resetResponse();
                user.handleCommand(messageReceived, userId);
                sendNotificationIfNecessary(user);
            } else {
                user = new User(userId);
                users.add(user);
                user.welcome();
            }
            sendResponse(userId, user.getResponse());
            if (user.getStatus() == Status.TAKE_TURN && user.isMyTurn()
                    && user.getGame().isReady()) {
                sendMenu(userId);
            } else if (user.getStatus() == Status.UNINITIALIZED){
                sendTitleMenu(user.getId());
            }
        } catch (Exception e) {
            sendResponse(userId, "Something went wrong in the backend :( Try again later");
            System.out.println("Exception caught! " + e.getMessage());
        }
    }

    private void sendNotificationIfNecessary(User user) {
        if (!user.isReadyToNotify()) {
            return;
        }

        System.out.println("Sending notification to " + user.getOpponentId());
        int userIndex = userExists(user.getOpponentId());
        User opponent = users.get(userIndex);

        String response = "";
        if (user.getStatus() == Status.JOINED) {
            response = "Other player has joined! Your turn: \n\n";
            user.setStatus(Status.TAKE_TURN);
            response += opponent.getGameState();
        } else if (user.sendGameOverMessage == true) {
            response += gameOverMessage(user);
            users.remove(user);
            user.resetResponse();
            sendResponse(user.getId(), response);

            users.remove(opponent);
            sendResponse(opponent.getId(), response);
            return;
        } else if (user.getGame().isEnded()) {
            System.out.println("Sending notification to opponent" + opponent.getId());
            sendResponse(opponent.getId(), "Game was ended by opponent... They must be scared to lose!");
            opponent.setStatus(Status.UNINITIALIZED);
            sendTitleMenu(opponent.getId());
            return;
        }
        else {
            response = "Other player has played! Your turn: \n\n";
            response += opponent.getGameState();
        }

        sendResponse(opponent.getId(), response);
        sendMenu(opponent.getId());
        user.wasNotified();
    }

    private String gameOverMessage(User user) {
        Game game = user.getGame();
        Player p1 = user.getGame().getPlayerOne();
        Player p2 = user.getGame().getPlayerTwo();
        String result = "Game over, the final score is... \n\n";

        result += game.getScore() + "\n";

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
}

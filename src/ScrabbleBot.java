import dictionary.Dictionary;
import gameplay.User;
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
            messageReceived = update.getMessage().getText();
            userId = update.getMessage().getFrom().getId();

            int userIndex = userExists(userId);
            User user;
            // If user exists, let him process the request
            if (userIndex >= 0) {
                user = users.get(userIndex);
                user.resetResponse();
                user.handleCommand(messageReceived, userId);
//                sendNotificationIfNecessary(user);
            }
            else {
                user = new User(userId);
                users.add(user);
                user.welcome();
            }
            sendResponse(userId, user.getResponse());
        }
        catch (Exception e) {
            sendResponse(userId, "Something went wrong in the backend :( Try again later");
            System.out.println("Exception caught! " + e.getMessage());
        }
    }

    private void sendNotificationIfNecessary(User user) {
        if (!user.isReadyToNotify()) {
            return;
        }

        System.out.println("Sending notification to " + user.getOpponentId());
        String response = "Other player has played! Your turn: \n\n";
        int userIndex = userExists(user.getOpponentId());
        User opponent = users.get(userIndex);

        response += opponent.getGameState();

        sendResponse(opponent.getId(), response);
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
}

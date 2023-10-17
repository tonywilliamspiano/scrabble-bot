import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrabbleBot extends TelegramLongPollingBot {
    List<User> users = new ArrayList<>();

    ScrabbleBot() {
    }
    @Override
    public void onUpdateReceived(Update update) {
        try {
            long chatId = update.getMessage().getChatId();
            String messageReceived = update.getMessage().getText();
            long userId = update.getMessage().getFrom().getId();

            int userIndex = userExists(userId);
            User user;
            // If user exists, let him process the request
            if (userIndex >= 0) {
                user = users.get(userIndex);
                user.resetResponse();
                user.parseCommand(messageReceived, userId);
            }
            else {
                user = new User(userId, chatId);
                users.add(user);
                user.welcome();
            }
            sendResponse(userId, user.getResponse());
        }
        catch (Exception e) {
            System.out.println("Exception caught! " + e.getMessage());
        }
    }

    private int userExists(long userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                return i;
            }
        }
        return -1;
    }

    private boolean isValidGameId(String messageReceived) {
        return false;
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

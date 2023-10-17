import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.List;

public class ScrabbleBot extends TelegramLongPollingBot {
    Game game = new Game();

    ScrabbleBot() {
        game.addPlayer(new Player("Tony"));
        game.addPlayer(new Player("Moritz"));
    }
    @Override
    public void onUpdateReceived(Update update) {
        try {
            long chatId = update.getMessage().getChatId();
            String messageReceived = update.getMessage().getText();

            takeTurn(messageReceived, chatId);
        }
        catch (Exception e) {
            System.out.println("Exception caught! " + e.getMessage());
        }
    }

    private void takeTurn(String messageReceived, long chatId) {
        Move move = MoveParser.parseMove(messageReceived);
        game.addWord(move);
        game.getPlayerOne().makeMove(move);

        sendResponse(chatId, game.getScore());
        sendResponse(chatId, game.boardAsString());
        sendResponse(chatId, game.showPlayerOneHand());
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

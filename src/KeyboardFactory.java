import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {
    public static InlineKeyboardMarkup getMenuKeyboard() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        // Create 'play' button
        InlineKeyboardButton playButton = new InlineKeyboardButton();
        playButton.setText("Play");
        playButton.setCallbackData("play");
        row.add(playButton);

        // Create 'swap' button
        InlineKeyboardButton swapButton = new InlineKeyboardButton();
        swapButton.setText("Swap");
        swapButton.setCallbackData("swap");
        row.add(swapButton);

        // Create 'pass' button
        InlineKeyboardButton passButton = new InlineKeyboardButton();
        passButton.setText("Pass");
        passButton.setCallbackData("pass");
        row.add(passButton);

        // Create 'show letter values' button
        InlineKeyboardButton letterButton = new InlineKeyboardButton();
        letterButton.setText("Show letter values");
        letterButton.setCallbackData("values");
        row2.add(letterButton);

        // Create exit button
        InlineKeyboardButton exitButton = new InlineKeyboardButton();
        exitButton.setText("Exit");
        exitButton.setCallbackData("exit");
        row2.add(exitButton);

        keyboard.add(row);
        keyboard.add(row2);
        inlineKeyboard.setKeyboard(keyboard);

        return inlineKeyboard;
    }

    public static InlineKeyboardMarkup getFirstKeyboard() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        // Create 'start' button
        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("start");
        startButton.setCallbackData("start");
        row.add(startButton);

        // Create 'join' button
        InlineKeyboardButton joinButton = new InlineKeyboardButton();
        joinButton.setText("join");
        joinButton.setCallbackData("join");
        row.add(joinButton);


        keyboard.add(row);
        inlineKeyboard.setKeyboard(keyboard);

        return inlineKeyboard;
    }

    public static InlineKeyboardMarkup getLetterKeyboard() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        // Create 'show letter values' button
        InlineKeyboardButton letterButton = new InlineKeyboardButton();
        letterButton.setText("Show letter values");
        letterButton.setCallbackData("values");
        row.add(letterButton);

        keyboard.add(row);
        inlineKeyboard.setKeyboard(keyboard);

        return inlineKeyboard;
    }
}

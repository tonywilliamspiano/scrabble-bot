import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {
    public static InlineKeyboardMarkup getMenuKeyboard() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        // Create 'play' button
        InlineKeyboardButton playButton = new InlineKeyboardButton();
        playButton.setText("Play");
        playButton.setCallbackData("play"); // This data will be sent to your bot when the button is clicked
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

        keyboard.add(row);
        inlineKeyboard.setKeyboard(keyboard);

        return inlineKeyboard;
    }
}

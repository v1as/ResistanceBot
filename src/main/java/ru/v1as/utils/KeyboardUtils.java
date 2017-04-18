package ru.v1as.utils;

import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class KeyboardUtils {

    public static ArrayList<KeyboardRow> getKeyboardRows(String[][] buttons) {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        for (String[] button : buttons) {
            KeyboardRow row = new KeyboardRow();
            Arrays.stream(button).map(KeyboardButton::new).forEach(row::add);
            keyboard.add(row);
        }
        return keyboard;
    }

    public static List<List<InlineKeyboardButton>> getInlineKeyboardRows(String[][] buttons) {
        ArrayList<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (String[] button : buttons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            Arrays.stream(button).map(b -> {
                InlineKeyboardButton r = new InlineKeyboardButton();
                r.setText(b);
                return r;
            }).forEach(row::add);
            keyboard.add(row);
        }
        return keyboard;
    }

}

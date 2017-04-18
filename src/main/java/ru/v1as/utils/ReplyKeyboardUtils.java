package ru.v1as.utils;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class ReplyKeyboardUtils {

    public static ReplyKeyboard replyKeyboard(String[][] buttons) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = KeyboardUtils.getKeyboardRows(buttons);
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setOneTimeKeyboad(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard replyButton(String button) {
        return replyKeyboard(new String[][]{new String[]{button}});
    }
}

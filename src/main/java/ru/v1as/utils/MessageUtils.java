package ru.v1as.utils;

import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class MessageUtils {

    public static SendMessage message(Long chatId, String text) {
        return message(chatId, text, null);
    }

    public static SendMessage message(Long chatId, String text, ReplyKeyboard keyboard) {
        SendMessage request = new SendMessage();
        request.setChatId(chatId);
        if (keyboard != null) {
            request.setReplyMarkup(keyboard);
        }
        request.setText(text);
        request.setParseMode(ParseMode.HTML);
        return request;
    }
}

package ru.v1as.utils;

import emoji4j.Emoji;
import emoji4j.EmojiUtils;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.v1as.model.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static ru.v1as.utils.GameUtils.getPeopleInMission;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class InlineKeyboardUtils {

    public static final Emoji CHECK = EmojiUtils.getEmoji("white_check_mark");
    public static final String DONE = "Закончить";
    public static final String YES = "Да";
    public static final String NO = "Нет";
    public static final String SUCCESS = "Пройти";
    public static final String FAIL = "Завалить";

    public static ReplyKeyboard replyKeyboard(String[][] buttons) {
        List<List<InlineKeyboardButton>> inlineButtons = KeyboardUtils.getInlineKeyboardRows(buttons);
        return wrap(inlineButtons);
    }

    public static ReplyKeyboard replyButtonUrl(String button, String url) {
        InlineKeyboardButton o = new InlineKeyboardButton();
        o.setText(button);
        o.setUrl(url);
        List<List<InlineKeyboardButton>> buttons = singletonList(singletonList(o));
        return wrap(buttons);
    }


    private static InlineKeyboardMarkup wrap(List<List<InlineKeyboardButton>> inlineButtons) {
        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(inlineButtons);
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup missionUsers(Game game) {
        List<User> missionUsers = game.getMissionUsers();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (User user : game.getUsers()) {
            String userButton = (missionUsers.contains(user) ? CHECK.getEmoji() : "") + Utils.user(user);
            String callbackData = game.getId() + ":" + user.getId().toString();
            buttons.add(inlineKeyboardButtonList(userButton, callbackData));
        }

        Integer peopleInMission = getPeopleInMission(game);
        if (missionUsers.size() == peopleInMission) {
            buttons.add(inlineKeyboardButtonList(DONE, game.getId() + ":" + DONE));
        }
        return wrap(buttons);
    }


    private static List<InlineKeyboardButton> inlineKeyboardButtonList(String userButton, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(userButton);
        button.setCallbackData(callbackData);
        return singletonList(button);
    }

    public static InlineKeyboardMarkup missionVoting(Game game) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(inlineKeyboardButtonList(YES, game.getId() + ":" + YES));
        buttons.add(inlineKeyboardButtonList(NO, game.getId() + ":" + NO));
        return wrap(buttons);
    }

    public static InlineKeyboardMarkup missionExecuting(Game game) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(inlineKeyboardButtonList(SUCCESS, game.getId() + ":" + SUCCESS));
        buttons.add(inlineKeyboardButtonList(FAIL, game.getId() + ":" + FAIL));
        return wrap(buttons);
    }

    public static InlineKeyboardMarkup empty() {
        return wrap(Collections.emptyList());
    }
}

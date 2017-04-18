package ru.v1as.utils;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import ru.v1as.model.Constants;
import ru.v1as.model.Game;

import java.util.stream.Collectors;

/**
 * Created by ivlasishen
 * on 13.04.2017.
 */
public class Utils {
    public static String user(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    public static boolean support(Chat chat) {
        return chat != null && (chat.isGroupChat() || chat.isSuperGroupChat());
    }

    public static boolean messageCheck(String constant, String message) {
        return constant.equals(message) || (constant + "@" + Constants.MY_NICK).equals(message);
    }

    public static String getRoles(Game game) {
        return game.getRoles().entrySet().stream().
                map(e -> user(e.getKey()) + ":" + e.getValue().toString()).
                collect(Collectors.joining("\n"));
    }
}

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
        String names;
        if (user.getFirstName() != null) {
            names = user.getFirstName() + " " + user.getLastName();
        } else if (user.getUserName() != null) {
            names = user.getUserName();
        } else {
            names = String.valueOf(user.getId());
        }
        return names;
    }

    public static boolean support(Chat chat) {
        return chat != null && (chat.isGroupChat() || chat.isSuperGroupChat());
    }

    public static String getRoles(Game game) {
        return game.getRoles().entrySet().stream().
                map(e -> user(e.getKey()) + ":" + e.getValue().toString()).
                collect(Collectors.joining("\n"));
    }

}

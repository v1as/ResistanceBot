package ru.v1as.utils;

import org.telegram.telegrambots.api.objects.User;
import ru.v1as.model.Constants;
import ru.v1as.model.Game;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class GameUtils {
    public static Integer getPeopleInMission(Game game) {
        int userAmount = game.getUsers().size();
        return Constants.USER_AMOUNT_2_MISSION_2_PEOPLE.get(userAmount)[game.getMissionIndex()];
    }

    public static boolean uEquals(User user1, User user2) {
        return user1.getId().equals(user2.getId());
    }
}

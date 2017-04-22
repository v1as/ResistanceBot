package ru.v1as.action;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.User;
import ru.v1as.model.*;
import ru.v1as.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

import static ru.v1as.model.GameState.*;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class TryStartGameRunnable extends AbstractSessionRunnable<Game> {

    public TryStartGameRunnable(AbstractSessionRunnable that) {
        super(that);
    }

    public TryStartGameRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        List<User> users = session.getUsers();
        if (users.size() < 2) {//todo 5
            session.setState(NOT_STARTED);
            message(session.getChatId(), "Мало людей");
            storage.deleteSession(session);
        } else {
            message(session.getChatId(), "Игра начинается!");
            session.setState(ROLES_SETTING);
            Integer spiesAmount = Constants.USERS_2_SPY.get(session.getUsers().size());
            session.getUsers().forEach(u -> session.getRoles().put(u, Role.Resistance));
            Set<User> spies = new HashSet<>();
            for (int i = 0; i < spiesAmount; i++) {
                User user;
                do {
                    user = users.get((int) (Math.random() * users.size()));
                } while (spies.contains(user));
                session.getRoles().put(user, Role.Spy);
                spies.add(user);
            }
            String spyList = "Шпионы: " + spies.stream().map(Utils::user).collect(Collectors.joining("\n"));
            for (User user : session.getUsers()) {
                Role role = session.getRoles().get(user);
                String text = "Твоя роль: " + role + " \n";
                if (Role.Spy.equals(role)) {
                    text += spyList;
                }
                message(user, text);
                session.getRoles().put(user, role);
            }
            task(new LeaderChangingRunnable(this), new DateTime());
        }
    }

    @Override
    Collection<GameState> getSupportedStates() {
        return Collections.singleton(JOINING);
    }

}

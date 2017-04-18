package ru.v1as.action;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.User;
import ru.v1as.model.Constants;
import ru.v1as.model.Game;
import ru.v1as.model.Role;
import ru.v1as.model.Storage;
import ru.v1as.utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.v1as.model.GameState.*;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class TryStartGameRunnable extends AbstractGameRunnable {

    public TryStartGameRunnable(AbstractGameRunnable that) {
        super(that);
    }

    public TryStartGameRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        if (!JOINING.equals(this.game.getState())) {
            return;
        }
        List<User> users = game.getUsers();
        if (users.size() < 1) {
            game.setState(NOT_STARTED);
            processor.add(Action.message("Мало людей", game.getChatId()));
            storage.deleteGame(game);
        } else {
            processor.add(Action.message("Игра начинается!", game.getChatId()));
            game.setState(ROLES_SETTING);
            Integer spiesAmount = Constants.USERS_2_SPY.get(game.getUsers().size());
            game.getUsers().forEach(u -> game.getRoles().put(u, Role.Resistance));
            Set<User> spies = new HashSet<>();
            for (int i = 0; i < spiesAmount; i++) {
                User user;
                do {
                    user = users.get((int) (Math.random() * users.size()));
                } while (spies.contains(user));
                game.getRoles().put(user, Role.Spy);
                spies.add(user);
            }
            String spyList = "Шпионы: " + spies.stream().map(Utils::user).collect(Collectors.joining("\n"));
            for (User user : game.getUsers()) {
                Role role = game.getRoles().get(user);
                String text = "Твоя роль: " + role + " \n";
                if (Role.Spy.equals(role)) {
                    text += spyList;
                }
                processor.add(Action.message(text, storage.getUserChat(user)));
                game.getRoles().put(user, role);
            }
            processor.add(Action.task(game, new LeaderChangingRunnable(this), new DateTime()));
        }
    }
}

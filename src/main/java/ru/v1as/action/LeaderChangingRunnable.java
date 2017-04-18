package ru.v1as.action;

import org.telegram.telegrambots.api.objects.User;
import ru.v1as.model.Game;
import ru.v1as.model.Storage;
import ru.v1as.utils.Utils;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static ru.v1as.model.GameState.MISSION_VOTES_CHECKING;
import static ru.v1as.model.GameState.SET_LEADER;
import static ru.v1as.utils.GameUtils.uEquals;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class LeaderChangingRunnable extends AbstractGameRunnable {

    public LeaderChangingRunnable(AbstractGameRunnable that) {
        super(that);
    }

    public LeaderChangingRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        if (!check(MISSION_VOTES_CHECKING) && this.game.getLeader() != null) {
            return;
        }
        game.setState(SET_LEADER);
        game.setLeaderChanged(game.getLeaderChanged() + 1);
        if (game.getLeaderChanged() == 5) {
            task(new GameFinishedRunnable(this));
            return;
        }
        List<User> users = game.getUsers();
        int index;
        if (game.getLeader() == null) {
            index = (int) (users.size() * Math.random());
        } else {
            index = IntStream.range(0, users.size()).
                    filter(u -> uEquals(game.getLeader(), users.get(u))).findFirst().orElse(0);
            index = ++index % users.size();
        }
        User leader = users.get(index);
        game.setLeader(leader);
        message(format("Лидером назначается: %s", Utils.user(leader)));
        task(new MissionGatheringRunnable(this));

    }

}

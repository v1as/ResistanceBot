package ru.v1as.action;

import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;
import ru.v1as.utils.Utils;

/**
 * Created by ivlasishen
 * on 18.04.2017.
 */
public class GameFinishedRunnable extends AbstractSessionRunnable<Game> {

    public GameFinishedRunnable(AbstractSessionRunnable that) {
        super(that);
    }

    public GameFinishedRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        if (session.getLeaderChanged() == 5) {
            message("Лидер был сменен в пятый раз. Игра закончилась - победили шпионы!");
            session.setState(GameState.FINISHED);
        }
        if (session.getMissionSucceeded() == 3) {
            message("Игра закончена - сопротивленцы победили!");
            session.setState(GameState.FINISHED);
        }
        if (session.getMissionFailed() == 3) {
            message("Игра закончена - шпионы победили!");
            session.setState(GameState.FINISHED);
        }
        if (check(GameState.FINISHED)) {
            message("роли: " + Utils.getRoles(session));
            storage.deleteSession(session);
        }
    }
}

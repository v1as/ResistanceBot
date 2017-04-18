package ru.v1as.action;

import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;
import ru.v1as.utils.Utils;

/**
 * Created by ivlasishen
 * on 18.04.2017.
 */
public class GameFinishedRunnable extends AbstractGameRunnable {

    public GameFinishedRunnable(AbstractGameRunnable that) {
        super(that);
    }

    public GameFinishedRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        if (game.getLeaderChanged() == 5) {
            message("Лидер был сменен в пятый раз. Игра закончилась - победили шпионы!");
            game.setState(GameState.FINISHED);
        }
        if (game.getMissionSucceeded() == 3) {
            message("Игра закончена - сопротивленцы победили!");
            game.setState(GameState.FINISHED);
        }
        if (game.getMissionFailed() == 3) {
            message("Игра закончена - шпионы победили!");
            game.setState(GameState.FINISHED);
        }
        if (check(GameState.FINISHED)) {
            message("роли: " + Utils.getRoles(game));
            storage.deleteGame(game);
        }
    }
}

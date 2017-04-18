package ru.v1as.action;

import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;

/**
 * Created by ivlasishen
 * on 18.04.2017.
 */
public class MissionExecutionResultsRunnable extends AbstractGameRunnable {

    public MissionExecutionResultsRunnable(AbstractGameRunnable that) {
        super(that);
    }

    public MissionExecutionResultsRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        if (!check(GameState.MISSION)) {
            return;
        }
        long countFails = game.getVotes().stream().filter(v -> v.getVote() != Boolean.TRUE).count();
        if (countFails == 0) {
            message("Поздравляю! Вы прошли миссию.");
            game.setMissionSucceeded(game.getMissionSucceeded() + 1);
        } else {
            message("Вы провалили миссию. Число заваленых этапов: " + countFails);
            game.setMissionFailed(game.getMissionFailed() + 1);
        }
        if (game.getMissionSucceeded() == 3 || game.getMissionFailed() == 3) {
            task(new GameFinishedRunnable(this));
        } else {
            task(new MissionGatheringRunnable(this));
        }
    }
}

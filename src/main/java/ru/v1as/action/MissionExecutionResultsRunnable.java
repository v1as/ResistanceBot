package ru.v1as.action;

import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;

import java.util.Collection;
import java.util.Collections;

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
        game.setState(GameState.MISSION_RESULTS);
        long countFails = game.getVotes().stream().filter(v -> v.getVote() != Boolean.TRUE).count();
        if (countFails == 0) {
            game.setMissionSucceeded(game.getMissionSucceeded() + 1);
            message("Поздравляю! Вы прошли миссию.");
        } else {
            game.setMissionFailed(game.getMissionFailed() + 1);
            message("Вы провалили миссию. Число заваленых этапов: " + countFails);
        }
        if (game.getMissionSucceeded() == 3 || game.getMissionFailed() == 3) {
            task(new GameFinishedRunnable(this));
        } else {
            task(new MissionGatheringRunnable(this));
        }
    }

    @Override
    Collection<GameState> getSupportedStates() {
        return Collections.singleton(GameState.MISSION);
    }
}

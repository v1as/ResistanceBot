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
public class MissionExecutionResultsRunnable extends AbstractSessionRunnable<Game> {

    public MissionExecutionResultsRunnable(AbstractSessionRunnable that) {
        super(that);
    }

    public MissionExecutionResultsRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        session.setState(GameState.MISSION_RESULTS);
        long countFails = session.getVotes().stream().filter(v -> v.getVote() != Boolean.TRUE).count();
        if (countFails == 0) {
            session.setMissionSucceeded(session.getMissionSucceeded() + 1);
            message("Поздравляю! Вы прошли миссию.");
        } else {
            session.setMissionFailed(session.getMissionFailed() + 1);
            message("Вы провалили миссию. Число заваленых этапов: " + countFails);
        }
        if (session.getMissionSucceeded() == 3 || session.getMissionFailed() == 3) {
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

package ru.v1as.action;

import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.MissionVote;
import ru.v1as.model.Storage;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class MissionVotingResultRunnable extends AbstractSessionRunnable<Game> {

    public MissionVotingResultRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    public MissionVotingResultRunnable(AbstractSessionRunnable that) {
        super(that);
    }

    @Override
    void gameRun() {
        session.setState(GameState.MISSION_VOTES_CHECKING);
        long votesYes = session.getVotes().stream().
                map(MissionVote::getVote).
                filter(Objects::nonNull).
                filter(v -> v).count();
        if ((session.usersAmount() - 1) < votesYes * 2) {
            message(session.getChatId(), "Состав миссии утвержден.");
            task(new MissionStartRunnable(this));
        } else {
            message(session.getChatId(), "Состав миссии не утвержден, идёт смена лидера.");
            task(new LeaderChangingRunnable(this));
        }
    }

    @Override
    Collection<GameState> getSupportedStates() {
        return Collections.singleton(GameState.MISSION_VOTING);
    }
}

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
public class MissionVotingResultRunnable extends AbstractGameRunnable {

    public MissionVotingResultRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    public MissionVotingResultRunnable(AbstractGameRunnable that) {
        super(that);
    }

    @Override
    void gameRun() {
        game.setState(GameState.MISSION_VOTES_CHECKING);
        long votesYes = game.getVotes().stream().
                map(MissionVote::getVote).
                filter(Objects::nonNull).
                filter(v -> v).count();
        if (game.usersAmount() < votesYes * 2) {
            this.processor.add(Action.message("Состав миссии утвержден.", game.getChatId()));
            this.processor.add(Action.task(game, new MissionStartRunnable(this)));
        } else {
            this.processor.add(Action.message("Состав миссии не утвержден, идёт смена лидера.", game.getChatId()));
            this.processor.add(Action.task(game, new LeaderChangingRunnable(this)));
        }
    }

    @Override
    Collection<GameState> getSupportedStates() {
        return Collections.singleton(GameState.MISSION_VOTING);
    }
}

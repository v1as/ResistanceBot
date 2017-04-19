package ru.v1as.action;

import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.MissionVote;
import ru.v1as.model.Storage;
import ru.v1as.utils.InlineKeyboardUtils;
import ru.v1as.utils.Utils;

import java.util.Collection;
import java.util.Collections;

import static ru.v1as.utils.GameUtils.uEquals;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class MissionVotingRunnable extends AbstractSessionRunnable<Game> {

    public MissionVotingRunnable(AbstractSessionRunnable that) {
        super(that);
    }

    public MissionVotingRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        session.setState(GameState.MISSION_VOTING);
        session.clearVoter();
        InlineKeyboardMarkup votingKeyboard = InlineKeyboardUtils.missionVoting(session);
        String messageText = "Лидер решил, что на миссию идут: ";
        for (User user : session.getMissionUsers()) {
            messageText += Utils.user(user) + "\n";
        }
        messageText += "Утвердить набор?";
        for (User user : session.getUsers()) {
            if (!uEquals(user, session.getLeader())) {
                message(messageText, votingKeyboard, user);
                session.addVoter(new MissionVote(user));
            }
        }
    }

    @Override
    Collection<GameState> getSupportedStates() {
        return Collections.singleton(GameState.MISSION_GATHERING);
    }
}

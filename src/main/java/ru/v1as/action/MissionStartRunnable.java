package ru.v1as.action;

import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.MissionVote;
import ru.v1as.model.Storage;
import ru.v1as.utils.InlineKeyboardUtils;

import java.util.Collection;
import java.util.Collections;

import static ru.v1as.model.GameState.MISSION;
import static ru.v1as.model.GameState.MISSION_VOTES_CHECKING;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class MissionStartRunnable extends AbstractSessionRunnable<Game> {

    public MissionStartRunnable(AbstractSessionRunnable that) {
        super(that);
    }

    public MissionStartRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        session.setState(MISSION);
        session.clearVoter();
        InlineKeyboardMarkup keyboard = InlineKeyboardUtils.missionExecuting(session);
        for (User user : session.getMissionUsers()) {
            String messageText = "Пройти или завалить миссию?";
            message(messageText, keyboard, user);
            session.addVoter(new MissionVote(user));
        }
    }

    @Override
    Collection<GameState> getSupportedStates() {
        return Collections.singleton(MISSION_VOTES_CHECKING);
    }
}

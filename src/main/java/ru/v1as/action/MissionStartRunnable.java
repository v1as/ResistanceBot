package ru.v1as.action;

import org.telegram.telegrambots.api.objects.Message;
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
public class MissionStartRunnable extends AbstractGameRunnable {

    public MissionStartRunnable(AbstractGameRunnable that) {
        super(that);
    }

    public MissionStartRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        game.setState(MISSION);
        game.clearVoter();
        InlineKeyboardMarkup keyboard = InlineKeyboardUtils.missionExecuting(game);
        for (User user : game.getMissionUsers()) {
            String messageText = "Пройти или завалить миссию?";
            Message message = processor.sendMessageToChat(messageText, storage.getUserChat(user), keyboard);
            game.addVoter(new MissionVote(user, message));
        }
    }

    @Override
    Collection<GameState> getSupportedStates() {
        return Collections.singleton(MISSION_VOTES_CHECKING);
    }
}

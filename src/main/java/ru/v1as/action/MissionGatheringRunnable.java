package ru.v1as.action;

import com.google.common.collect.Sets;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;
import ru.v1as.utils.InlineKeyboardUtils;

import java.util.Collection;

import static ru.v1as.model.GameState.*;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class MissionGatheringRunnable extends AbstractSessionRunnable<Game> {

    public MissionGatheringRunnable(AbstractSessionRunnable that) {
        super(that);
    }

    public MissionGatheringRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        session.setState(MISSION_GATHERING);
        session.getMissionUsers().clear();
        Long leaderChatId = storage.getUserChat(session.getLeader());
        InlineKeyboardMarkup keyboard = InlineKeyboardUtils.missionUsers(session);
        processor.sendMessageToChat("Выбери кого ты хочешь взять на миссию:", leaderChatId, keyboard);
    }

    @Override
    Collection<GameState> getSupportedStates() {
        return Sets.newHashSet(SET_LEADER, MISSION_RESULTS);
    }
}

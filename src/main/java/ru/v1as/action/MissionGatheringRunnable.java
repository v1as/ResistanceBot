package ru.v1as.action;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;
import ru.v1as.utils.InlineKeyboardUtils;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class MissionGatheringRunnable extends AbstractGameRunnable {

    public MissionGatheringRunnable(AbstractGameRunnable that) {
        super(that);
    }

    public MissionGatheringRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        if (!check(GameState.SET_LEADER) && !check(GameState.MISSION_GATHERING)) {
            return;
        }
        game.setState(GameState.MISSION_GATHERING);
        Long leaderChatId = storage.getUserChat(game.getLeader());
        InlineKeyboardMarkup keyboard = InlineKeyboardUtils.missionUsers(game);
        if (game.getGatheringMessage() == null) {
            Message message = processor.sendMessageToChat("Выбери кого ты хочешь взять на миссию:", leaderChatId, keyboard);
            game.setGatheringMessage(message);
        } else {
            processor.editMessage(leaderChatId, game.getGatheringMessage().getMessageId(), keyboard);
        }
    }

}

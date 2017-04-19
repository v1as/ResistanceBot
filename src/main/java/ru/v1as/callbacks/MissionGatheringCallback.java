package ru.v1as.callbacks;

import com.google.common.collect.Maps;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import ru.v1as.action.ActionProcessor;
import ru.v1as.action.MissionVotingRunnable;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;
import ru.v1as.utils.GameUtils;
import ru.v1as.utils.InlineKeyboardUtils;

import java.util.List;
import java.util.Map;

import static ru.v1as.utils.InlineKeyboardUtils.DONE;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class MissionGatheringCallback extends AbstractCallbackHandler<Game> {

    public MissionGatheringCallback(Storage storage, ActionProcessor processor) {
        super(GameState.MISSION_GATHERING, storage, processor);
    }

    @Override
    public void handle(CallbackQuery update, Game game, String data) {
        if (update.getFrom().getId().equals(game.getLeader().getId())) {
            Map<String, User> id2User = Maps.uniqueIndex(game.getUsers(), u -> u.getId().toString());
            List<User> missionUsers = game.getMissionUsers();
            Message message = update.getMessage();
            if (data.equals(DONE)) {
                if (GameUtils.getPeopleInMission(game) == missionUsers.size()) {
                    editKeyboard(message.getChatId(), message.getMessageId(), InlineKeyboardUtils.empty());
                    task(game, new MissionVotingRunnable(game, storage, processor));
                }
            } else {
                User newUser = id2User.get(data);
                if (newUser == null) {
                    return;
                }
                if (missionUsers.contains(newUser)) {
                    missionUsers.remove(newUser);
                } else {
                    if (missionUsers.size() < GameUtils.getPeopleInMission(game)) {
                        missionUsers.add(newUser);
                    }
                }
                editKeyboard(message.getChatId(), message.getMessageId(), InlineKeyboardUtils.missionUsers(game));
            }
        }
    }

}

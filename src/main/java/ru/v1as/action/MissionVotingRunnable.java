package ru.v1as.action;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.MissionVote;
import ru.v1as.model.Storage;
import ru.v1as.utils.InlineKeyboardUtils;
import ru.v1as.utils.Utils;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class MissionVotingRunnable extends AbstractGameRunnable {

    public MissionVotingRunnable(AbstractGameRunnable that) {
        super(that);
    }

    public MissionVotingRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(game, storage, processor);
    }

    @Override
    void gameRun() {
        if (!check(GameState.MISSION_GATHERING)) {
            return;
        }
        game.setState(GameState.MISSION_VOTING);
        game.clearVoter();
        InlineKeyboardMarkup votingKeyboard = InlineKeyboardUtils.missionVoting(game);
        String messageText = "Лидер решил, что на миссию идут: ";
        for (User user : game.getMissionUsers()) {
            messageText += Utils.user(user) + "\n";
        }
        messageText += "Утвердить набор?";
        for (User user : game.getUsers()) {
//            if (!user.getId().equals(game.getLeader().getId())) { //todo
            Message message = processor.sendMessageToChat(messageText, storage.getUserChat(user), votingKeyboard);
            game.addVoter(new MissionVote(user, message));
//            }
        }
    }
}

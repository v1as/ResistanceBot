package ru.v1as.callbacks;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import ru.v1as.action.ActionProcessor;
import ru.v1as.action.MissionVotingResultRunnable;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.MissionVote;
import ru.v1as.model.Storage;
import ru.v1as.utils.InlineKeyboardUtils;

import static ru.v1as.utils.GameUtils.uEquals;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class MissionGatheringVotingCallback extends AbstractCallbackHandler<Game> {

    public MissionGatheringVotingCallback(Storage storage, ActionProcessor processor) {
        super(GameState.MISSION_VOTING, storage, processor);
    }

    @Override
    public void handle(CallbackQuery update, Game game, String datum) {
        User from = update.getFrom();
        if (!uEquals(from, game.getLeader()) &&
                game.getUsersId().contains(from.getId())) {
            MissionVote vote = null;
            if (InlineKeyboardUtils.YES.equals(datum)) {
                vote = game.setVote(from, true);
            } else if (InlineKeyboardUtils.NO.equals(datum)) {
                vote = game.setVote(from, false);
            }
            if (vote != null) {
                Message message = update.getMessage();
                Long chatId = message.getChatId();
                editKeyboard(chatId, message.getMessageId(), InlineKeyboardUtils.empty());
                String missionResult = vote.getVote() ? "утвердить" : "отклонить";
                message(chatId, "Выбор сделан. Набор для миссии: " + missionResult);
            }
            if (game.getVotes().size() >= game.getUsers().size() - 1) {//TODO
                task(game, new MissionVotingResultRunnable(game, storage, processor));
            }

        }
    }

}
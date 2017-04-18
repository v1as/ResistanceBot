package ru.v1as.callbacks;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import ru.v1as.action.ActionProcessor;
import ru.v1as.action.MissionExecutionResultsRunnable;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.MissionVote;
import ru.v1as.model.Storage;
import ru.v1as.utils.GameUtils;
import ru.v1as.utils.InlineKeyboardUtils;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class MissionExecutingCallback extends AbstractCallbackHandler {

    public MissionExecutingCallback(Storage storage, ActionProcessor processor) {
        super(GameState.MISSION, storage, processor);
    }

    @Override
    public void handle(CallbackQuery update, Game game, String data) {
        Integer userId = update.getFrom().getId();
        if (game.getMissionUserIds().contains(userId)) {
            MissionVote vote = null;
            if (InlineKeyboardUtils.SUCCESS.equals(data)) {
                vote = game.setVote(update.getFrom(), true);
            } else if (InlineKeyboardUtils.FAIL.equals(data)) {
                vote = game.setVote(update.getFrom(), false);
            }
            if (vote != null) {
                Message message = vote.getMessage();
                Long chatId = message.getChatId();
                processor.editMessage(chatId, message.getMessageId(), InlineKeyboardUtils.empty());
                String text = "Выбор сделан. Вы " + (vote.getVote() ? "выполнили" : "завалили") +
                        " свою часть миссии для миссии:";
                processor.sendMessageToChat(text, chatId);
                if (GameUtils.getPeopleInMission(game) == game.getVotes().size()) {
                    task(game, new MissionExecutionResultsRunnable(game, storage, processor));
                }
            }

        }
    }
}

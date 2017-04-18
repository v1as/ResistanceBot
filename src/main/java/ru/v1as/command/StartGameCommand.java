package ru.v1as.command;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.v1as.action.Action;
import ru.v1as.action.ActionProcessor;
import ru.v1as.action.TryStartGameRunnable;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;

import static ru.v1as.utils.MessageUtils.message;
import static ru.v1as.utils.Utils.support;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class StartGameCommand extends AbstractBotCommand {

    public StartGameCommand(Storage storage, ActionProcessor processor) {
        super(storage, processor, "/startgame", "start new game");
    }

    @Override
    public void executeUnsafe(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException {
        if (!support(chat)) {
            absSender.sendMessage(message(chat.getId(), "Эта команда работает только в групповом чате"));
        } else {
            if (!storage.hasGame(chat.getId())) {
                Game newGame = new Game(chat);
                storage.addGame(chat.getId(), newGame);
                newGame.setState(GameState.JOINING);
                processor.sendMessageToChat("Давайте попытаемся собрать игроков, жми /join чтобы присоединиться!", chat.getId());
                addStartingActions(newGame);
            }
        }
    }

    private void addStartingActions(Game game) {
        processor.add(Action.message("До начала игры минута!", game.getChatId(), new DateTime().plusSeconds(5)));
        processor.add(Action.message("До начала игры 30 секунд!", game.getChatId(), new DateTime().plusSeconds(10)));
        processor.add(Action.task(game, new TryStartGameRunnable(game, storage, processor), new DateTime().plusSeconds(15)));
    }

}

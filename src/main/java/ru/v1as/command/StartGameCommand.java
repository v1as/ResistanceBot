package ru.v1as.command;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.v1as.action.ActionProcessor;
import ru.v1as.action.TryStartGameRunnable;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;

import static ru.v1as.utils.Utils.support;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class StartGameCommand extends AbstractBotCommand<Game> {

    public static final String START_GAME = "/startgame";
    private JoinCommand<Game> joinCommand;

    public StartGameCommand(Storage<Game> storage, ActionProcessor processor) {
        super(storage, processor, START_GAME, "start new session");
        joinCommand = new JoinCommand<>(storage, processor);

    }

    @Override
    public void executeUnsafe(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException {
        if (!support(chat)) {
            message(chat.getId(), "Эта команда работает только в групповом чате");
        } else {
            if (!storage.hasSession(chat.getId())) {
                Game newGame = new Game(chat);
                storage.addSession(chat.getId(), newGame);
                newGame.setState(GameState.JOINING);
                message(chat.getId(), "Давайте попытаемся собрать игроков, жми /join чтобы присоединиться!");
                addStartingActions(newGame);
                joinCommand.executeUnsafe(null, user, chat, null);

            }
        }
    }

    private void addStartingActions(Game game) {
        message(game.getChatId(), "До начала игры минута!", new DateTime().plusSeconds(5));
        message(game.getChatId(), "До начала игры 30 секунд!", new DateTime().plusSeconds(10));
        task(game, new TryStartGameRunnable(game, storage, processor), new DateTime().plusSeconds(15));
    }

}

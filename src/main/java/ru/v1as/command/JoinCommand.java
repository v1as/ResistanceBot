package ru.v1as.command;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.v1as.action.Action;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Game;
import ru.v1as.model.Storage;

import static java.lang.String.format;
import static ru.v1as.model.GameState.JOINING;
import static ru.v1as.utils.Utils.user;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class JoinCommand extends AbstractBotCommand {

    public JoinCommand(Storage storage, ActionProcessor processor) {
        super(storage, processor, "/join", "Присоединиться к игре");
    }

    @Override
    void executeUnsafe(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException {
        Game g = storage.game(chat.getId());
        if (g == null) {
            processor.add(Action.message("Игра не была запущена", chat.getId()));
            return;
        }
        if (!JOINING.equals(g.getState())) {
            processor.add(Action.message("Набор игроков закончен", chat.getId()));
            return;
        }
        if (g.containsUser(user)) {
            processor.add(Action.message(format("Игрок %s уже добавлен в игру!", user(user)), chat.getId()));
        } else if (g.getUsers().size() < 10) {
            g.getUsers().add(user);
            processor.add(Action.message(format("Игрок %s успешно добавлен в игру.", user(user)), chat.getId()));
            processor.add(Action.message(format("Ты был успешно добавлен в игру в группе \"%s\"", g.getChatTitle()),
                    storage.getUserChat(user)));
        }
    }

}

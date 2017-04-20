package ru.v1as.command;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.GroupSession;
import ru.v1as.model.Session;
import ru.v1as.model.Storage;

import static java.lang.String.format;
import static ru.v1as.model.GameState.JOINING;
import static ru.v1as.utils.Utils.user;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class JoinCommand<GroupSessionImpl extends GroupSession> extends AbstractBotCommand<GroupSessionImpl> {

    public JoinCommand(Storage<GroupSessionImpl> storage, ActionProcessor processor) {
        super(storage, processor, "/join", "Присоединиться к игре");
    }

    @Override
    void executeUnsafe(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException {
        Long chatId = chat.getId();
        GroupSession g = storage.session(chatId);
        if (g == null) {
            message(chatId, "Игра не была запущена");
            return;
        }
        if (!JOINING.equals(g.getState())) {
            message(chatId, "Набор игроков закончен");
            return;
        }
        if (g.containsUser(user)) {
            message(chatId, format("Игрок %s уже добавлен в игру!", user(user)));
        } else if (g.getUsers().size() < 10) {
            g.getUsers().add(user);
            message(chatId, format("Игрок %s успешно добавлен в игру.", user(user)));
            message(user, format("Ты был успешно добавлен в игру в группе \"%s\"", g.getChat().getTitle()));
        }
    }

}

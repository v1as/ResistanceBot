package ru.v1as.command;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Storage;

/**
 * Created by ivlasishen
 * on 18.04.2017.
 */
public class ClearAllGames extends AbstractBotCommand {

    public ClearAllGames(Storage storage, ActionProcessor processor) {
        super(storage, processor, "/cleargames", "Удалить все игры");
    }

    @Override
    void executeUnsafe(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException {
        synchronized (this) {
            storage.clearGames();
        }
    }
}

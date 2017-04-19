package ru.v1as;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.action.Action;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Game;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class AbstractGameService {

    protected final ActionProcessor processor;

    public AbstractGameService(ActionProcessor processor) {
        this.processor = processor;
    }

    protected void task(Game game, Runnable task) {
        processor.add(Action.task(game, task));
    }

    protected void message(Long chatId, String text) {
        processor.add(Action.message(text, chatId));
    }

    protected void editKeyboard(Long chatId, Integer messageId, InlineKeyboardMarkup keyboardMarkup) {
        processor.add(Action.editKeyboard(messageId, chatId, keyboardMarkup, new DateTime()));
    }
}

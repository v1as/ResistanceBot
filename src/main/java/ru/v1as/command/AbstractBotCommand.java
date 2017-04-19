package ru.v1as.command;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.v1as.action.Action;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Constants;
import ru.v1as.model.Game;
import ru.v1as.model.Session;
import ru.v1as.model.Storage;
import ru.v1as.utils.InlineKeyboardUtils;
import ru.v1as.utils.Utils;

import static ru.v1as.ResistanceBot.CONTACT_ME;
import static ru.v1as.utils.Utils.user;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public abstract class AbstractBotCommand<SessionImpl extends Session> extends BotCommand {

    protected final Storage<SessionImpl> storage;
    protected final ActionProcessor processor;

    public AbstractBotCommand(Storage<SessionImpl> storage, ActionProcessor processor, String commandIdentifier, String description) {
        super(commandIdentifier, description);
        this.storage = storage;
        this.processor = processor;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            if (checkUser(absSender, user, chat)) {
                return;
            }
            SessionImpl session = storage.session(chat.getId());
            System.out.println(String.format("Command %s from user %s for session %s",
                    getCommandIdentifier(), Utils.user(user), session != null ? session.getId() : null));
            if (session != null) {
                synchronized (session) {
                    executeUnsafe(absSender, user, chat, arguments);
                }
            } else {
                executeUnsafe(absSender, user, chat, arguments);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean checkUser(AbsSender absSender, User user, Chat chat) {
        if (!storage.knownUser(user)) {
            InlineKeyboardMarkup keyboard = InlineKeyboardUtils.replyButtonUrl("Start Me", Constants.URL_TO_ME);
            String text = String.format(CONTACT_ME, user(user));
            processor.sendMessageToChat(text, chat.getId(), keyboard);
            return true;
        }
        return false;
    }

    abstract void executeUnsafe(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException;

    protected void task(Game game, Runnable task) {
        processor.add(Action.task(game, task));
    }

    protected void task(Game game, Runnable task, DateTime dateTime) {
        processor.add(Action.task(game, task, dateTime));
    }

    protected void message(Long chatId, String text) {
        processor.add(Action.message(text, chatId));
    }

    protected void message(User user, String text) {
        Long chatId = storage.getUserChat(user);
        processor.add(Action.message(text, chatId));
    }

    protected void message(Long chatId, String text, DateTime dateTime) {
        processor.add(Action.message(text, chatId, dateTime));
    }

    protected void editKeyboard(Long chatId, Integer messageId, InlineKeyboardMarkup keyboardMarkup) {
        processor.add(Action.editKeyboard(messageId, chatId, keyboardMarkup, new DateTime()));
    }

}

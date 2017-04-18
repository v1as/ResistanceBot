package ru.v1as.command;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Constants;
import ru.v1as.model.Game;
import ru.v1as.model.Storage;
import ru.v1as.utils.InlineKeyboardUtils;
import ru.v1as.utils.Utils;

import static ru.v1as.ResistanceBot.CONTACT_ME;
import static ru.v1as.utils.Utils.user;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public abstract class AbstractBotCommand extends BotCommand {

    protected final Storage storage;
    protected final ActionProcessor processor;

    public AbstractBotCommand(Storage storage, ActionProcessor processor, String commandIdentifier, String description) {
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
            Game game = storage.game(chat.getId());
            System.out.println(String.format("Command %s from user %s for game %s",
                    getCommandIdentifier(), Utils.user(user), game != null ? game.getId() : null));
            if (game != null) {
                synchronized (game) {
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
            ReplyKeyboard keyboard = InlineKeyboardUtils.replyButtonUrl("Start Me", Constants.URL_TO_ME);
            String text = String.format(CONTACT_ME, user(user));
            processor.sendMessageToChat(text, chat.getId(), keyboard);
            return true;
        }
        return false;
    }

    private void checkUser(User user, Long chatId) {
        /*boolean result = false;

        if (update.getMessage().isUserMessage()) {
            if (!userId2ChatId.containsKey(user.getId())) {
                userId2ChatId.put(user.getId(), chatId);
                sendMessageToChat("Я тебя запомнил, спасибо.", chatId);
            }
        } else {
            if (!userId2ChatId.containsKey(user.getId())) {

                sendMessageToChat(text, chatId, );
                result = true;
            }
        }
        return result;*/
    }

    abstract void executeUnsafe(AbsSender absSender, User user, Chat chat, String[] arguments) throws TelegramApiException;

}

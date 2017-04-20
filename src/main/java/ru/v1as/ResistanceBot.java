package ru.v1as;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.CommandRegistry;
import org.telegram.telegrambots.generics.LongPollingBot;
import ru.v1as.action.ActionProcessor;
import ru.v1as.callbacks.CallbackProcessor;
import ru.v1as.callbacks.MissionExecutingCallback;
import ru.v1as.callbacks.MissionGatheringCallback;
import ru.v1as.callbacks.MissionGatheringVotingCallback;
import ru.v1as.command.ClearAllGames;
import ru.v1as.command.JoinCommand;
import ru.v1as.command.StartGameCommand;
import ru.v1as.model.Constants;
import ru.v1as.model.Game;
import ru.v1as.model.Storage;

/**
 * Created by ivlasishen
 * on 12.04.2017.
 */
public class ResistanceBot extends TelegramLongPollingBot implements LongPollingBot {

    public static final String CONTACT_ME = "%s, добавь меня в контакты и напиши мне что-нибудь!";
    public static String TOKEN = "310998804:AAGYcYIcN4Bqo04iSu-S0_0uiGgK46JGvGM";

    private final CommandRegistry commandRegistry;
    private final ActionProcessor processor;
    private Storage<Game> storage;
    private CallbackProcessor<Game> callbackHandler;


    public ResistanceBot(ActionProcessor processor) {
        this.processor = processor;
        storage = new Storage<>();
        commandRegistry = new CommandRegistry(true, getBotUsername());
        callbackHandler = new CallbackProcessor<>(storage, processor);
        this.register(new StartGameCommand(storage, processor));
        this.register(new JoinCommand<>(storage, processor));
        this.register(new ClearAllGames(storage, processor));
        callbackHandler.register(new MissionGatheringCallback(storage, processor));
        callbackHandler.register(new MissionGatheringVotingCallback(storage, processor));
        callbackHandler.register(new MissionExecutingCallback(storage, processor));
    }

    @Override
    public final void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isCommand()) {
                if (commandRegistry.executeCommand(this, message)) {
                    return;
                }
            }
        }
        processNonCommandUpdate(update);
    }

    public void processNonCommandUpdate(Update update) {
        storage.registerUserIfNeed(update);
        callbackHandler.handle(update);
    }

    public final boolean register(BotCommand botCommand) {
        return commandRegistry.register(botCommand);
    }


    @Override
    public String getBotUsername() {
        return Constants.MY_NICK;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    public void processActions() {
        this.processor.processActions();
    }

    public Storage<Game> getStorage() {
        return storage;
    }
}

package ru.v1as;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.generics.LongPollingBot;
import ru.v1as.action.ActionProcessor;
import ru.v1as.callbacks.CallbackProcessor;
import ru.v1as.callbacks.MissionExecutingCallback;
import ru.v1as.callbacks.MissionGatheringCallback;
import ru.v1as.callbacks.MissionVotingCallback;
import ru.v1as.command.ClearAllGames;
import ru.v1as.command.JoinCommand;
import ru.v1as.command.StartGameCommand;
import ru.v1as.model.Constants;
import ru.v1as.model.Storage;

/**
 * Created by ivlasishen
 * on 12.04.2017.
 */
public class ResistanceBot extends TelegramLongPollingCommandBot implements LongPollingBot {

    public static final String CONTACT_ME = "%s, добавь меня в контакты и напиши мне что-нибудь!";
    public static String TOKEN = "310998804:AAGYcYIcN4Bqo04iSu-S0_0uiGgK46JGvGM";
    private ActionProcessor processor = new ActionProcessor(this);
    private Storage storage = new Storage();
    private CallbackProcessor callbackHandler = new CallbackProcessor(storage, processor);

    {
        this.register(new StartGameCommand(storage, processor));
        this.register(new JoinCommand(storage, processor));
        this.register(new ClearAllGames(storage, processor));
        callbackHandler.register(new MissionGatheringCallback(storage, processor));
        callbackHandler.register(new MissionVotingCallback(storage, processor));
        callbackHandler.register(new MissionExecutingCallback(storage, processor));
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        storage.registerUserIfNeed(update);
        callbackHandler.handle(update);
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
}

package ru.v1as.action;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.AbstractGameService;
import ru.v1as.model.GameState;
import ru.v1as.model.Session;
import ru.v1as.model.Storage;

import java.util.Collection;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public abstract class AbstractSessionRunnable<StateImpl extends Session> extends AbstractGameService implements Runnable {

    final protected StateImpl session;
    final protected Storage<StateImpl> storage;
    final protected ActionProcessor processor;

    public AbstractSessionRunnable(AbstractSessionRunnable<StateImpl> that) {
        this(that.session, that.storage, that.processor);
    }

    public AbstractSessionRunnable(StateImpl game, Storage storage, ActionProcessor processor) {
        super(processor);
        this.session = game;
        this.storage = storage;
        this.processor = processor;
    }

    @Override
    public void run() {
        String runnable = this.getClass().getSimpleName();
        synchronized (session) {
            if (storage.containSession(session)) {
                if (getSupportedStates() != null && !getSupportedStates().contains(session.getState())) {
                    System.out.println(String.format("This runnable %s does not support this state %s for session %s",
                            runnable, session.getState(), session.getId()));
                    return;
                }
                System.out.println(String.format("Runnable: %s for session %s", session.getId(), runnable));
                gameRun();
            }
        }
    }

    abstract void gameRun();

    protected void message(String messageText) {
        processor.add(Action.message(messageText, session.getChatId()));
    }

    protected void message(User user, String messageText) {
        Long chatId = storage.getUserChat(user);
        processor.add(Action.message(messageText, chatId));
    }

    protected void message(String messageText, InlineKeyboardMarkup keyboard, User user) {
        Long chatId = storage.getUserChat(user);
        processor.add(Action.message(messageText, chatId, keyboard, new DateTime()));
    }

    protected void task(Runnable task) {
        processor.add(Action.task(session, task));
    }

    protected void task(Runnable task, DateTime dateTime) {
        processor.add(Action.task(session, task, dateTime));
    }

    protected boolean check(GameState gameState) {
        return this.session.getState().equals(gameState);
    }

    Collection<GameState> getSupportedStates() {
        return null;
    }

}

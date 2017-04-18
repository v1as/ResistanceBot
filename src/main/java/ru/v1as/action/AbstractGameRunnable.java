package ru.v1as.action;

import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import ru.v1as.AbstractGameService;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;

import java.util.Collection;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public abstract class AbstractGameRunnable extends AbstractGameService implements Runnable {

    final protected Game game;
    final protected Storage storage;
    final protected ActionProcessor processor;

    public AbstractGameRunnable(AbstractGameRunnable that) {
        this(that.game, that.storage, that.processor);
    }

    public AbstractGameRunnable(Game game, Storage storage, ActionProcessor processor) {
        super(processor);
        this.game = game;
        this.storage = storage;
        this.processor = processor;
    }

    @Override
    public void run() {
        String runnable = this.getClass().getSimpleName();
        synchronized (game) {
            if (storage.containGame(game)) {
                if (getSupportedStates() != null && !getSupportedStates().contains(game.getState())) {
                    System.out.println(String.format("This runnable %s does not support this state %s for game %s",
                            runnable, game.getState(), game.getId()));
                    return;
                }
                System.out.println(String.format("Executing for game %s runnable: %s", game.getId(), runnable));
                gameRun();
            }
        }
    }

    abstract void gameRun();

    protected void message(String messageText) {
        processor.add(Action.message(messageText, game.getChatId()));
    }

    protected void message(String messageText, User user) {
        Long chatId = storage.getUserChat(user);
        processor.add(Action.message(messageText, chatId));
    }

    protected void message(String messageText, ReplyKeyboard keyboard, User user) {
        Long chatId = storage.getUserChat(user);
        processor.add(Action.message(messageText, chatId, keyboard, new DateTime()));
    }

    protected void task(Runnable task) {
        processor.add(Action.task(game, task));
    }

    protected boolean check(GameState gameState) {
        return this.game.getState().equals(gameState);
    }

    Collection<GameState> getSupportedStates() {
        return null;
    }

}

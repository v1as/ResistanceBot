package ru.v1as.callbacks;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.v1as.AbstractGameService;
import ru.v1as.action.Action;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public abstract class AbstractCallbackHandler extends AbstractGameService implements CallbackHandler {

    protected final Storage storage;
    protected final GameState state;

    protected AbstractCallbackHandler(GameState state, Storage storage, ActionProcessor processor) {
        super(processor);
        this.state = state;
        this.storage = storage;
    }

    public abstract void handle(CallbackQuery update, Game game, String datum);

    @Override
    public GameState getGameState() {
        return state;
    }

    protected void task(Game game, Runnable task) {
        processor.add(Action.task(game, task));
    }
}

package ru.v1as.callbacks;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import ru.v1as.AbstractGameService;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Session;
import ru.v1as.model.SessionState;
import ru.v1as.model.Storage;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public abstract class AbstractCallbackHandler<SessionImpl extends Session> extends AbstractGameService implements CallbackHandler {

    protected final Storage<SessionImpl> storage;
    protected final SessionState state;

    protected AbstractCallbackHandler(SessionState state, Storage storage, ActionProcessor processor) {
        super(processor);
        this.state = state;
        this.storage = storage;
    }

    public abstract void handle(CallbackQuery update, SessionImpl session, String datum);

    @Override
    public SessionState getGameState() {
        return state;
    }

}

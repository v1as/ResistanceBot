package ru.v1as.callbacks;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Session;
import ru.v1as.model.SessionState;
import ru.v1as.model.Storage;
import ru.v1as.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class CallbackProcessor<SessionImpl extends Session> {

    private final Storage<SessionImpl> storage;
    private final ActionProcessor processor;
    private final Map<SessionState, AbstractCallbackHandler<SessionImpl>> handlers;

    public CallbackProcessor(Storage<SessionImpl> storage, ActionProcessor processor) {
        this.storage = storage;
        this.processor = processor;
        this.handlers = new HashMap<>();
    }

    public void handle(Update update) {
        if (!isValid(update)) {
            System.out.println("This callback is not valid.");
            return;
        }
        String[] data = update.getCallbackQuery().getData().split(":");
        SessionImpl session = storage.session(data[0]);
        if (session != null) {
            synchronized (session) {
                AbstractCallbackHandler<SessionImpl> handler = handlers.get(session.getState());
                String from = Utils.user(update.getCallbackQuery().getFrom());
                String handlerName = handler != null ? handler.getClass().getSimpleName() : "null";
                System.out.println(String.format("Callback %s by user %s for game %s with data %s",
                        handlerName, from, session.getId(), data[1]));
                if (handler != null) {
                    handler.handle(update.getCallbackQuery(), session, data[1]);
                }
            }
        }
    }

    private boolean isValid(Update update) {
        CallbackQuery callback = update.getCallbackQuery();
        boolean result = callback != null;
        if (result) {
            String data = callback.getData();
            String[] split = data.split(":");
            result = split.length == 2 && storage.containSession(split[0]);
        }
        return result;
    }

    public void register(AbstractCallbackHandler handler) {
        handlers.put(handler.getGameState(), handler);
    }

}

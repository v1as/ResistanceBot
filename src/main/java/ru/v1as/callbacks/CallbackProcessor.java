package ru.v1as.callbacks;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import ru.v1as.action.ActionProcessor;
import ru.v1as.model.Game;
import ru.v1as.model.GameState;
import ru.v1as.model.Storage;
import ru.v1as.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class CallbackProcessor {

    private final Storage storage;
    private final ActionProcessor processor;
    private final Map<GameState, AbstractCallbackHandler> handlers;

    public CallbackProcessor(Storage storage, ActionProcessor processor) {
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
        Game game = storage.game(data[0]);
        if (game != null) {
            synchronized (game) {
                AbstractCallbackHandler handler = handlers.get(game.getState());
                String from = Utils.user(update.getCallbackQuery().getFrom());
                String handlerName = handler != null ? handler.getClass().getSimpleName() : "null";
                System.out.println(String.format("Game %s handled by user %s execute %s with data %s",
                        game.getId(), from, handlerName, data[1]));
                if (handler != null) {
                    handler.handle(update.getCallbackQuery(), game, data[1]);
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
            result = split.length == 2 && storage.containGame(split[0]);
        }
        return result;
    }

    public void register(AbstractCallbackHandler handler) {
        handlers.put(handler.getGameState(), handler);
    }

}

package ru.v1as.action;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import ru.v1as.model.Game;

import java.util.Objects;

/**
 * Created by ivlasishen
 * on 13.04.2017.
 */
public class Action {

    private DateTime date;
    private Runnable task;
    private Long chatId;
    private String messageText;
    private ReplyKeyboard keyboard;
    private ActionType type;

    private Action() {
    }

    public static Action message(String text, Long chatId) {
        return message(text, chatId, new DateTime());
    }

    public static Action message(String text, Long chatId, DateTime date) {
        Action result = new Action();
        result.messageText = text;
        result.chatId = chatId;
        result.date = date;
        result.type = ActionType.Message;
        return result;
    }

    public static Action message(String text, Long chatId, ReplyKeyboard keyboard, DateTime date) {
        Action result = new Action();
        result.messageText = text;
        result.chatId = chatId;
        result.date = date;
        result.keyboard = keyboard;
        result.type = ActionType.Message;
        return result;
    }

    public static Action task(Game game, Runnable runnable, DateTime date) {
        Action result = new Action();
        result.task = () -> {
            synchronized (game) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        result.date = date;
        result.type = ActionType.Task;
        return result;
    }

    public static Action task(Game game, Runnable runnable) {
        return task(game, runnable, new DateTime());
    }

    public static Action merge(Action t, Action t1) {
        Preconditions.checkArgument(t.isMessage() && t1.isMessage());
        Preconditions.checkArgument(Objects.equals(t.chatId, t1.chatId));
        return Action.message(Joiner.on("\n\n").join(t.getMessageText(), t1.getMessageText()), t.getChatId());
    }

    public DateTime getDate() {
        return date;
    }

    public Runnable getTask() {
        return task;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getMessageText() {
        return messageText;
    }

    public boolean isMessage() {
        return ActionType.Message.equals(type);
    }

    public boolean isTask() {
        return ActionType.Task.equals(type);
    }

    public boolean ready(DateTime now) {
        return !date.isAfter(now);
    }

    public ReplyKeyboard getKeyboard() {
        return keyboard;
    }
}

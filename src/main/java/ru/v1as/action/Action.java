package ru.v1as.action;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.v1as.model.Session;

import java.util.*;

/**
 * Created by ivlasishen
 * on 13.04.2017.
 */
public class Action {

    private List<String> ids;
    private DateTime date;
    private Runnable task;
    private Long chatId;
    private String messageText;
    private InlineKeyboardMarkup keyboard;
    private ActionType type;
    private Integer messageId;

    private Action() {
        this.ids = Collections.singletonList(UUID.randomUUID().toString());
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

    public static Action message(String text, Long chatId, InlineKeyboardMarkup keyboard, DateTime date) {
        Action result = new Action();
        result.messageText = text;
        result.chatId = chatId;
        result.date = date;
        result.keyboard = keyboard;
        result.type = ActionType.Message;
        return result;
    }

    public static Action editKeyboard(Integer messageId, Long chatId, InlineKeyboardMarkup keyboard, DateTime date) {
        Action result = new Action();
        result.messageId = messageId;
        result.chatId = chatId;
        result.date = date;
        result.keyboard = keyboard;
        result.type = ActionType.KeyboardEdit;
        return result;
    }

    public static Action task(Session session, Runnable runnable, DateTime date) {
        Action result = new Action();
        result.task = () -> {
            synchronized (session) {
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

    public static Action task(Session session, Runnable runnable) {
        return task(session, runnable, new DateTime());
    }

    public static Action merge(Action t, Action t1) {
        Preconditions.checkArgument(t.isMessage() && t1.isMessage());
        Preconditions.checkArgument(Objects.equals(t.chatId, t1.chatId));
        Action message = Action.message(Joiner.on("\n\n").join(t.getMessageText(), t1.getMessageText()), t.getChatId());
        ArrayList<String> ids = new ArrayList<>();
        ids.addAll(t.getIds());
        ids.addAll(t1.getIds());
        message.setIds(ids);
        return message;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
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

    public boolean isKeyboardEditing() {
        return ActionType.KeyboardEdit.equals(type);
    }

    public boolean isTask() {
        return ActionType.Task.equals(type);
    }

    public boolean ready(DateTime now) {
        return !date.isAfter(now);
    }

    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
}

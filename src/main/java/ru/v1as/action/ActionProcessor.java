package ru.v1as.action;

import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class ActionProcessor {

    public static final int MAX_MESSAGE_IN_SECOND = 30;
    private final AbsSender sender;
    private List<Action> actions = new CopyOnWriteArrayList<>();
    private Executor executor = Executors.newFixedThreadPool(5);

    public ActionProcessor(AbsSender sender) {
        this.sender = sender;
    }

    public void addAll(List<Action> actions) {
        this.actions.addAll(actions);
    }

    public void add(Action action) {
        this.actions.add(action);
    }

    public void processActions() {
        try {
            DateTime now = new DateTime();
            if (actions.size() == 0) {
                return;
            }
            List<Action> toProcess = actions.stream().filter(a -> a.ready(now)).collect(Collectors.toList());
            toProcess.stream().filter(Action::isTask).forEach(a -> {
                executor.execute(a.getTask());
                actions.remove(a);
            });
            List<Action> toProcessNow = new ArrayList<>();
            toProcessNow.addAll(toProcess.stream().filter(Action::isKeyboardEditing).collect(Collectors.toList()));
            toProcessNow.addAll(mergeMessages(toProcess));
            toProcessNow = toProcessNow.stream().limit(MAX_MESSAGE_IN_SECOND).collect(Collectors.toList());
            toProcessNow.forEach(this::process);
            Set<String> toRemoveIds = toProcessNow.stream().
                    flatMap(a -> a.getIds().stream()).
                    collect(Collectors.toSet());
            List<Action> toDelete = actions.stream().
                    filter(a -> toRemoveIds.contains(a.getIds().get(0))).
                    collect(Collectors.toList());
            actions.removeAll(toDelete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<Action> mergeMessages(List<Action> toProcess) {
        return toProcess.stream().
                filter(Action::isMessage).
                collect(
                        Collectors.groupingBy(Action::getChatId,
                                Collectors.reducing(Action::merge)))
                .values().stream().map(Optional::get).collect(Collectors.toList());
    }

    private void process(Action action) {
        try {
            Preconditions.checkNotNull(action.getChatId());
            if (action.isMessage()) {
                Preconditions.checkNotNull(action.getMessageText());
                executor.execute(() -> sendMessageToChat(action.getMessageText(), action.getChatId(), action.getKeyboard()));
            } else {
                Preconditions.checkNotNull(action.getKeyboard());
                Preconditions.checkNotNull(action.getMessageId());
                executor.execute(() -> editMessage(action.getChatId(), action.getMessageId(), action.getKeyboard()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message sendMessageToChat(String text, Long chatId) {
        return sendMessageToChat(text, chatId, null);
    }

    public Message sendMessageToChat(String text, Long chatId, InlineKeyboardMarkup keyboard) {
        SendMessage request = new SendMessage();
        request.setChatId(chatId);
        if (keyboard != null) {
            request.setReplyMarkup(keyboard);
        }
        request.setText(text);
        request.setParseMode(ParseMode.HTML);
        try {
            return sender.sendMessage(request);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;//todo
    }


    public void editMessage(Long chatId, Integer messageId, InlineKeyboardMarkup keyboard) {
        EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
        replyMarkup.setMessageId(messageId);
        replyMarkup.setChatId(chatId);
        replyMarkup.setReplyMarkup(keyboard);
        try {
            this.sender.editMessageReplyMarkup(replyMarkup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

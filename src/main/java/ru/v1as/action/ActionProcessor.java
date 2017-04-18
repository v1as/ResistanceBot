package ru.v1as.action;

import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class ActionProcessor {

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
            toProcess.stream().filter(Action::isTask).forEach(a -> executor.execute(a.getTask()));
            toProcess.stream().
                    filter(Action::isMessage).
                    collect(
                            Collectors.groupingBy(Action::getChatId,
                                    Collectors.reducing(Action::merge)))
                    .values().stream().map(Optional::get).
                    forEach(this::process);
            actions.removeAll(toProcess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(Action action) {
        Preconditions.checkArgument(action.isMessage());
        Preconditions.checkNotNull(action.getMessageText());
        Preconditions.checkNotNull(action.getChatId());
        executor.execute(() -> sendMessageToChat(action.getMessageText(), action.getChatId(), action.getKeyboard()));
    }

    public Message sendMessageToChat(String text, Long chatId) {
        return sendMessageToChat(text, chatId, null);
    }

    public Message sendMessageToChat(String text, Long chatId, ReplyKeyboard keyboard) {
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

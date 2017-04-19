package ru.v1as.model;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivlasishen
 * on 14.04.2017.
 */
public class Storage<SessionImpl extends Session> {

    private Map<Long, SessionImpl> sessions = new HashMap<>();
    private Map<Integer, Long> userId2ChatId = new HashMap<>();

    public SessionImpl session(Long chatId) {
        return sessions.get(chatId);
    }

    public boolean hasSession(Long chatId) {
        return sessions.containsKey(chatId);
    }

    public void addSession(Long chatId, SessionImpl newGame) {
        sessions.put(chatId, newGame);
    }

    public boolean knownUser(User user) {
        return userId2ChatId.containsKey(user.getId());
    }

    public void registerUserIfNeed(Update update) {
        Message message = update.getMessage();
        if (message != null && message.getChat().isUserChat()) {
            User from = message.getFrom();
            Long chatId = message.getChatId();
            userId2ChatId.put(from.getId(), chatId);
        }
    }

    public Long getUserChat(User user) {
        return userId2ChatId.get(user.getId());
    }

    public boolean containSession(SessionImpl game) {
        return sessions.values().contains(game);
    }

    public void deleteSession(SessionImpl session) {
        Map.Entry<Long, SessionImpl> entry = sessions.entrySet().stream().
                filter(e -> e.getValue() == session).
                findAny().orElse(null);
        if (null != entry) {
            sessions.remove(entry.getKey());
        }
    }

    public boolean containSession(Object sessionId) {
        return sessions.values().stream().
                filter(g -> g.getId().equals(sessionId)).
                findAny().orElse(null) != null;
    }

    public SessionImpl session(Object sessionId) {
        return sessions.values().stream().
                filter(g -> g.getId().equals(sessionId)).
                findAny().orElse(null);
    }

    public void clearSessions() {
        this.sessions.clear();
    }
}

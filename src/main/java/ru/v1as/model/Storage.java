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
public class Storage {

    private static Map<Long, Game> games = new HashMap<>();
    private static Map<Integer, Long> userId2ChatId = new HashMap<>();

    public Game game(Long chatId) {
        return games.get(chatId);
    }

    public boolean hasGame(Long chatId) {
        return games.containsKey(chatId);
    }

    public void addGame(Long chatId, Game newGame) {
        games.put(chatId, newGame);
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

    public boolean containGame(Game game) {
        return games.values().contains(game);
    }

    public void deleteGame(Game game) {
        Map.Entry<Long, Game> entry = games.entrySet().stream().
                filter(e -> e.getValue() == game).
                findAny().orElse(null);
        if (null != entry) {
            games.remove(entry.getKey());
        }
    }

    public boolean containGame(String gameId) {
        return games.values().stream().
                filter(g -> g.getId().equals(gameId)).
                findAny().orElse(null) != null;
    }

    public Game game(String gameId) {
        return games.values().stream().
                filter(g -> g.getId().equals(gameId)).
                findAny().orElse(null);
    }

    public void clearGames() {
        this.games.clear();
    }
}

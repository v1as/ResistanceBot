package ru.v1as.model;

import org.telegram.telegrambots.api.objects.Chat;

/**
 * Created by ivlasishen
 * on 19.04.2017.
 */
public interface Session {

    SessionState getState();

    Object getId();

    Long getChatId();

    Chat getChat();

}

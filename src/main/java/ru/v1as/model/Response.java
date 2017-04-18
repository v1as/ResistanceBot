package ru.v1as.model;

import org.joda.time.DateTime;

/**
 * Created by ivlasishen
 * on 13.04.2017.
 */
public class Response {

    private String message;
    private Long chatId;
    private DateTime date;

    public Response(Long chatId, String message) {
        this.chatId = chatId;
        this.message = message;
    }

    public Response(String message, Long chatId, DateTime date) {
        this.message = message;
        this.chatId = chatId;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public boolean timeToSend() {
        return date == null || date.isAfter(DateTime.now());
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

}

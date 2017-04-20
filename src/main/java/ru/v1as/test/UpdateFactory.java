package ru.v1as.test;

import org.telegram.telegrambots.api.objects.*;
import ru.v1as.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by ivlasishen
 * on 19.04.2017.
 */
public class UpdateFactory {

    private static Map<User, Chat> privateChats = new HashMap<>();
    private static Integer messageCounter = 0;
    private static Long chatCounter = 0L;

    private static ReflectiveFactory<Update> updateFactory = new ReflectiveFactory<>(Update.class,
            "message",
            "callbackQuery"
    );

    private static ReflectiveFactory<Chat> chatFactory = new ReflectiveFactory<>(Chat.class,
            "id",
            "type",
            "title"
    );

    private static ReflectiveFactory<Message> messageFactory = new ReflectiveFactory<>(Message.class,
            "messageId",
            "from",
            "chat",
            "text",
            "entities"
    );

    private static ReflectiveFactory<CallbackQuery> callbackFactory = new ReflectiveFactory<>(CallbackQuery.class,
            "id",
            "from",
            "message"
    );

    private static ReflectiveFactory<MessageEntity> messageEntityFactory = new ReflectiveFactory<>(MessageEntity.class,
            "type",
            "text",
            "offset"
    );
    private static Chat publicChat;

    public static Update privateMessage(User from, String text) {
        return buildUpdate(buildMessage(from, getPrivateChat(from), text), null);
    }

    public static Update publicMessage(User from, String text) {
        return buildUpdate(buildMessage(from, getPublicChat(), text), null);
    }

    public static Update privateCallback(User from, String text) {
        return buildUpdate(null, buildCallback(from, buildMessage(from, getPrivateChat(from), text)));
    }

    public static Update publicCallback(User from, String text) {
        return buildUpdate(null, buildCallback(from, buildMessage(from, getPublicChat(), text)));
    }

    private static Chat getPrivateChat(User from) {
        return privateChats.computeIfAbsent(from, k -> buildPrivateChat(Utils.user(from)));
    }

    private static Update buildUpdate(Message message, CallbackQuery callbackQuery) {
        return updateFactory.buildEntity(message, callbackQuery);
    }

    private static Message buildMessage(Integer messageId, User from, Chat chat, String text) {
        return messageFactory.buildEntity(messageId, from, chat, text, new ArrayList<>());
    }

    private static Message buildMessage(User from, Chat chat, String text) {
        return buildMessage(messageCounter++, from, chat, text);
    }

    private static Chat buildChat(Long id, String type, String title) {
        return chatFactory.buildEntity(id, type, title);
    }

    private static Chat buildChat(String type, String title) {
        return chatFactory.buildEntity(chatCounter++, type, title);
    }

    private static Chat buildPrivateChat(String title) {
        return chatFactory.buildEntity(chatCounter++, "private", title);
    }

    private static CallbackQuery buildCallback(User from, Message message) {
        return buildCallback(UUID.randomUUID().toString(), from, message);
    }

    private static CallbackQuery buildCallback(String id, User from, Message message) {
        return callbackFactory.buildEntity(id, from, message);
    }

    private static MessageEntity buildMessageEntity(String type, String text) {
        return messageEntityFactory.buildEntity(type, text, 0);
    }

    public static Chat getPublicChat() {
        return publicChat == null ? publicChat = buildChat(chatCounter++, "group", "public chat") : publicChat;
    }

    public static Update command(Update update) {
        Message message = update.getMessage();
        message.getEntities().add(buildMessageEntity("bot_command", message.getText()));
        return update;
    }

    /*private static Chat buildChat(){

    }*/


}

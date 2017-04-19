package ru.v1as.model;

import org.telegram.telegrambots.api.objects.User;

import java.util.Collection;


/**
 * Created by ivlasishen
 * on 19.04.2017.
 */
public interface GroupSession extends Session {

    boolean containsUser(User user);

    Collection<User> getUsers();


}

package ru.v1as.test;

import org.telegram.telegrambots.api.objects.User;


/**
 * Created by ivlasishen
 * on 19.04.2017.
 */
public class UserFactory extends ReflectiveFactory<User> {

    private static Integer idSequance = 0;

    public UserFactory() {
        super(User.class, "id", "userName", "firstName", "lastName");
    }

    public User build(String userName) {
        return super.buildEntity(++idSequance, userName, userName + " first", userName + " last");
    }

}

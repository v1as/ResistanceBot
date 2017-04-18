package ru.v1as.model;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by ivlasishen
 * on 13.04.2017.
 */
public class Constants {

    public static final String TELEGRAM_URL = "telegram.me/";
    public static final String START_GAME = "/startgame";
    public static final String JOIN_GAME = "/join";
    public static final String MY_NICK = "resist_game_bot";
    public static final String URL_TO_ME = TELEGRAM_URL + MY_NICK;
    public static final Map<Integer, Integer> USERS_2_SPY = ImmutableMap.<Integer, Integer>builder().
            put(1, 1).
            put(2, 1).
            put(3, 1).
            put(4, 1).

            put(5, 2).
            put(6, 2).
            put(7, 3).
            put(8, 3).
            put(9, 3).
            put(10, 4).build();
    public static final Map<Integer, Integer[]> USER_AMOUNT_2_MISSION_2_PEOPLE = ImmutableMap.<Integer, Integer[]>
            builder().
            put(1, new Integer[]{1, 1, 1, 1, 1}).
            put(2, new Integer[]{1, 1, 1, 1, 1}).
            put(3, new Integer[]{1, 1, 1, 1, 1}).
            put(4, new Integer[]{1, 1, 1, 1, 1}).

            put(5, new Integer[]{2, 3, 2, 3, 3}).
            put(6, new Integer[]{2, 3, 4, 3, 4}).
            put(7, new Integer[]{2, 3, 3, 4, 4}).
            put(8, new Integer[]{3, 4, 4, 5, 6}).
            put(9, new Integer[]{3, 4, 4, 5, 6}).
            put(10, new Integer[]{3, 4, 4, 5, 6}).
            build();

}

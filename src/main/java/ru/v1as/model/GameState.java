package ru.v1as.model;

/**
 * Created by ivlasishen
 * on 13.04.2017.
 */
public enum GameState implements SessionState {
    NOT_STARTED,
    JOINING,
    ROLES_SETTING,
    SPY_MEET,
    SET_LEADER,
    MISSION_GATHERING,
    MISSION_VOTING,
    MISSION_VOTES_CHECKING,
    MISSION,
    MISSION_RESULTS,
    FINISHED
}

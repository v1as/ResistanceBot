package ru.v1as.model;

/**
 * Created by ivlasishen
 * on 13.04.2017.
 */
public enum Role {
    Spy, Resistance;


    @Override
    public String toString() {
        return Spy.equals(this) ? "Шпион" : "Сопротивленец";
    }

}

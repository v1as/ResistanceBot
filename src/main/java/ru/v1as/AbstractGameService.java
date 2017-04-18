package ru.v1as;

import ru.v1as.action.ActionProcessor;

/**
 * Created by ivlasishen
 * on 17.04.2017.
 */
public class AbstractGameService {
    protected final ActionProcessor processor;

    public AbstractGameService(ActionProcessor processor) {
        this.processor = processor;
    }
}

package ru.v1as.test;

import com.google.common.base.Preconditions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Created by ivlasishen
 * on 19.04.2017.
 */
public class ReflectiveFactory<T extends Object> {

    private Constructor<T> constructor;
    private String[] fieldNames;
    private Field[] fields;

    public ReflectiveFactory(Class<T> clazz, String... fields) {
        try {
            this.fieldNames = fields;
            this.constructor = clazz.getConstructor();
            this.fields = new Field[fields.length];
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i];
                this.fields[i] = clazz.getDeclaredField(field);
                this.fields[i].setAccessible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T buildEntity(Object... fieldValues) {
        Preconditions.checkArgument(fieldValues.length == this.fieldNames.length);
        T t = null;
        try {
            t = constructor.newInstance();
            for (int i = 0; i < fieldValues.length; i++) {
                Object fieldValue = fieldValues[i];
                this.fields[i].set(t, fieldValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

}

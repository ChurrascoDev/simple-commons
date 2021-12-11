package com.github.imthenico.simplecommons.util;

import java.util.Objects;

public interface Validate {

    static <T> T notNull(T obj, String msg) {
        return Objects.requireNonNull(obj, msg);
    }

    static <T> T notNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    static <T> T isTrue(boolean expression, String msg, T toReturn) {
        if (!expression)
            throw new IllegalArgumentException(msg);

        return toReturn;
    }

    static <T> T isTrue(boolean expression, T toReturn) {
        if (!expression)
            throw new IllegalArgumentException();

        return toReturn;
    }

    static void isTrue(boolean expression, String msg) {
        if (!expression)
            throw new IllegalArgumentException(msg);
    }

    static void isTrue(boolean expression) {
        if (!expression)
            throw new IllegalArgumentException();
    }

    static <T> T defIfNull(T obj, T def) {
        return obj == null ? def : obj;
    }
}
package com.github.imthenico.simplecommons.data.repository.exception;

import java.util.function.Supplier;

public class SerializationException extends Exception {

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static <T> T catchCause(Supplier<T> supplier) throws SerializationException {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new SerializationException("unable to serialize object", e);
        }
    }
}
package com.github.imthenico.simplecommons.data.exception;

public class IllegalValueTypeException extends RuntimeException {

    public IllegalValueTypeException(Object value) {
        super("Illegal value type: " + (value != null ? value.getClass().getName() : null));
    }
}
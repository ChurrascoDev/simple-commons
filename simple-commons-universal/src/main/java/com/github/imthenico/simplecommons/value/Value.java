package com.github.imthenico.simplecommons.value;

public interface Value {

    <T> T getValue();

    boolean mutable();

    MutableValue mutableCopy();

    AbstractValue immutableCopy();

    default boolean isNull() {
        return getValue() == null;
    }
}
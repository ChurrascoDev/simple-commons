package com.github.imthenico.simplecommons.value;

import java.util.Optional;

public interface AbstractValue extends Value {

    <T> Optional<T> cast();

    <T> Optional<T> cast(Class<T> clazz);

    Optional<String> getAsString();

    Optional<Character> getAsChar();

    Optional<Number> getAsNumber();

    Optional<Boolean> getAsBoolean();

}
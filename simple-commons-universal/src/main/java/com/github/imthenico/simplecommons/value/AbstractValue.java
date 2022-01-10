package com.github.imthenico.simplecommons.value;

import com.github.imthenico.simplecommons.util.list.ArrayContainer;

import java.util.Optional;

public interface AbstractValue extends Value {

    <T> Optional<T> cast();

    <T> Optional<T> cast(Class<T> clazz);

    Optional<String> getAsString();

    Optional<Character> getAsChar();

    Optional<Number> getAsNumber();

    Optional<Boolean> getAsBoolean();

    <E> Optional<ArrayContainer<E>> getAsArray();

}
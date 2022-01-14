package com.github.imthenico.simplecommons.value;

import java.util.Objects;
import java.util.Optional;

public final class SimpleAbstractValue implements AbstractValue {

    public static final SimpleAbstractValue EMPTY = new SimpleAbstractValue(null);

    private final Object value;

    public SimpleAbstractValue(Object value) {
        if (value != null) {
            if (!isValid(value))
                throw new UnsupportedOperationException("Illegal value type: " + value.getClass());
        }

        this.value = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> cast() {
        try {
            return Optional.ofNullable((T) value);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> cast(Class<T> clazz) {
        return clazz.isInstance(value) ? Optional.of((T) value) : Optional.empty();
    }

    @Override
    public Optional<String> getAsString() {
        return cast(String.class);
    }

    @Override
    public Optional<Character> getAsChar() {
        return cast(Character.class);
    }

    @Override
    public Optional<Number> getAsNumber() {
        return cast(Number.class);
    }

    @Override
    public Optional<Boolean> getAsBoolean() {
        return cast(Boolean.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public boolean mutable() {
        return false;
    }

    @Override
    public MutableValue mutableCopy() {
        return new SimpleMutableValue(this);
    }

    @Override
    public AbstractValue immutableCopy() {
        return new SimpleAbstractValue(value);
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }

    private boolean isValid(Object value) {
        for (Class<?> clazz : new Class[]{String.class, Character.class, Number.class, Boolean.class}) {
            if (clazz.isInstance(value))
                return true;
        }

        return false;
    }
}
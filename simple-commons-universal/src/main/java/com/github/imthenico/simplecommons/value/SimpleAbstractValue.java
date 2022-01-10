package com.github.imthenico.simplecommons.value;

import com.github.imthenico.simplecommons.util.list.ArrayContainer;

import java.util.Collection;
import java.util.Optional;

public class SimpleAbstractValue implements AbstractValue {

    protected Object value;

    public SimpleAbstractValue(Object value) {
        if (value != null) {
            checkType(value);

            this.value = value;
        }
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
    public <E> Optional<ArrayContainer<E>> getAsArray() {
        return value instanceof ArrayContainer ? Optional.of((ArrayContainer<E>) value)
                : value != null && value.getClass().isArray() ? Optional.of(new ArrayContainer<>((E[]) value)) : Optional.empty();
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

    protected void checkType(Object value) {
        if (value instanceof ArrayContainer) {
            for (Object o : ((ArrayContainer<?>) value)) {
                checkType(o);
            }
        } else if (value instanceof Collection) {
            for (Object o : ((Collection<?>) value)) {
                checkType(o);
            }
        } else if (value.getClass().isArray()) {
            for (Object o : (Object[]) value) {
                checkType(o);
            }
        } else {
            for (Class<?> clazz : new Class[]{String.class, Character.class, Number.class, Boolean.class}) {
                if (clazz.isInstance(value))
                    return;
            }
        }

        throw new UnsupportedOperationException("Unsupported type:" + value.getClass().getName());
    }
}
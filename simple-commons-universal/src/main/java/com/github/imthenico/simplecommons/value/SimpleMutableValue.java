package com.github.imthenico.simplecommons.value;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.Optional;

public class SimpleMutableValue implements MutableValue {

    private SimpleAbstractValue value;

    public SimpleMutableValue(Object value) {
        setValue(value);
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            this.value = new SimpleAbstractValue(value);
        } else {
            this.value = SimpleAbstractValue.EMPTY;
        }
    }

    @Override
    public <T> T getValue() {
        return Validate.defIfNull(value, SimpleAbstractValue.EMPTY).getValue();
    }

    @Override
    public boolean mutable() {
        return true;
    }

    @Override
    public MutableValue mutableCopy() {
        SimpleMutableValue mutableValue = new SimpleMutableValue(null);
        mutableValue.value = value;

        return mutableValue;
    }

    @Override
    public AbstractValue immutableCopy() {
        return value.immutableCopy();
    }

    @Override
    public <T> Optional<T> cast() {
        return value.cast();
    }

    @Override
    public <T> Optional<T> cast(Class<T> clazz) {
        return value.cast(clazz);
    }

    @Override
    public Optional<String> getAsString() {
        return value.getAsString();
    }

    @Override
    public Optional<Character> getAsChar() {
        return value.getAsChar();
    }

    @Override
    public Optional<Number> getAsNumber() {
        return value.getAsNumber();
    }

    @Override
    public Optional<Boolean> getAsBoolean() {
        return value.getAsBoolean();
    }
}
package com.github.imthenico.simplecommons.value;

public class SimpleMutableValue extends SimpleAbstractValue implements MutableValue {
    public SimpleMutableValue(Object value) {
        super(value);
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            checkType(value);

            this.value = value;
        }
    }

    @Override
    public boolean mutable() {
        return true;
    }
}
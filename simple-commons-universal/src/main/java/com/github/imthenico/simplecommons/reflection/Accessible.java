package com.github.imthenico.simplecommons.reflection;

import java.lang.reflect.AccessibleObject;

public abstract class Accessible<T extends AccessibleObject> {

    protected final T handle;

    protected Accessible(T handle) {
        this.handle = handle;
    }

    public Object getObject(Object obj, Object... args) {
        boolean oldAccessible = handle.canAccess(obj);
        handle.setAccessible(true);

        Object value = null;
        try {
            value = getValue(obj, args);
            handle.setAccessible(oldAccessible);
        } catch (Exception e) {
            handle.setAccessible(oldAccessible);
            e.printStackTrace();
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    public <O> O get(Object obj, Object... args) {
        return (O) getObject(obj, args);
    }

    protected abstract Object getValue(Object obj, Object... args) throws IllegalAccessException;

    public T getHandle() {
        return handle;
    }
}
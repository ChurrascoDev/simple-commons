package com.github.imthenico.simplecommons.object;

import java.util.Objects;

public class BasicTypeObject {

    private final Object object;

    public BasicTypeObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public Object[] getArray() {
        if (object.getClass().isArray())
            return (Object[]) object;

        throw handleInvalidObjectType();
    }

    public boolean asBoolean() {
        if (object instanceof Boolean)
            return (boolean) object;

        throw handleInvalidObjectType();
    }

    public Number asNumber() {
        if (object instanceof Number)
            return (Number) object;

        throw handleInvalidObjectType();
    }

    public String asString() {
        return Objects.toString(object);
    }

    private RuntimeException handleInvalidObjectType() {
        return new UnsupportedOperationException(String.format("object type: %s", object.getClass().getTypeName()));
    }
}
package com.github.imthenico.simplecommons.data.mapper.gson;

import com.github.imthenico.simplecommons.value.Value;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ValueSerializer implements JsonSerializer<Value> {
    @Override
    public JsonElement serialize(Value value, Type type, JsonSerializationContext context) {
        return context.serialize(value.getValue());
    }
}
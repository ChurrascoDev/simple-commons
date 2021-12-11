package com.github.imthenico.simplecommons.data.repository.mapper;

import com.github.imthenico.simplecommons.data.repository.GenericMapper;
import com.github.imthenico.simplecommons.data.repository.exception.SerializationException;
import com.github.imthenico.simplecommons.object.BasicTypeObject;
import com.github.imthenico.simplecommons.util.Validate;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.Map;

public class GsonMapper implements GenericMapper<String> {

    private final Gson gson;

    public GsonMapper(Gson gson) {
        this.gson = Validate.notNull(gson);
    }

    public GsonMapper() {
        this(new Gson());
    }

    @Override
    public <O> O map(String readableData, Class<O> targetClass) {
        return gson.fromJson(readableData, targetClass);
    }

    @Override
    public String serialize(Object o) throws SerializationException {
        return SerializationException.catchCause(() -> gson.toJson(o));
    }

    @Override
    public <O> O mapFields(Map<String, BasicTypeObject> data, Class<O> targetClass) {
        JsonObject jsonObject = newJsonObject(data);
        return gson.fromJson(jsonObject, targetClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> serializeFields(Object toSerialize) throws SerializationException {
        JsonElement jsonElement = gson.toJsonTree(toSerialize);

        return SerializationException.catchCause(() -> gson.fromJson(jsonElement, Map.class));
    }

    private JsonElement identify(Object object) {
        if (object instanceof String) {
            JsonReader jsonReader = new JsonReader(new StringReader((String) object));
            jsonReader.setLenient(true);

            return JsonParser.parseReader(jsonReader);
        } else if (object instanceof Number) {
            return new JsonPrimitive((Number) object);
        } else if (object instanceof Boolean) {
            return new JsonPrimitive((Boolean) object);
        } else if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            JsonArray jsonArray = new JsonArray();

            for (Object o : array) {
                jsonArray.add(identify(o));
            }
        }

        return JsonNull.INSTANCE;
    }

    private JsonObject newJsonObject(Map<String, BasicTypeObject> data) {
        if (data == null || data.isEmpty())
            return null;

        JsonObject jsonObject = new JsonObject();

        data.forEach((key, value) -> jsonObject.add(key, identify(value.getObject())));

        return jsonObject;
    }
}
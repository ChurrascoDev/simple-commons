package com.github.imthenico.simplecommons.data.mapper.gson;

import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.data.node.NodeValue;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.exception.SerializationException;
import com.github.imthenico.simplecommons.value.AbstractValue;
import com.github.imthenico.simplecommons.value.Value;
import com.google.gson.*;

import java.util.*;

public class GsonMapper implements GenericMapper<String> {

    private final Gson gson;

    public GsonMapper(GsonBuilder gson) {
        gson.registerTypeHierarchyAdapter(
                Value.class, new ValueSerializer()
        ).registerTypeHierarchyAdapter(
                NodeValue.class, new NodeValueTypeAdapter()
        ).registerTypeHierarchyAdapter(
                TreeNode.class, new TreeNodeTypeAdapter()
        );

        this.gson = gson.create();
    }

    public GsonMapper() {
        this(new GsonBuilder());
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
    public <O> O fromMap(Map<String, ?> data, Class<O> targetClass) {
        return gson.fromJson(gson.toJsonTree(data), targetClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, AbstractValue> toMap(Object toSerialize) throws SerializationException {
        JsonElement jsonElement = gson.toJsonTree(toSerialize);

        return SerializationException.catchCause(() -> gson.fromJson(jsonElement, Map.class));
    }
}
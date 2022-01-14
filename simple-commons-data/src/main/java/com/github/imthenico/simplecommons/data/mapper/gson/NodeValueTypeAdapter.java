package com.github.imthenico.simplecommons.data.mapper.gson;

import com.github.imthenico.simplecommons.data.node.NodeValue;
import com.github.imthenico.simplecommons.data.node.value.SimpleNodeValue;
import com.google.gson.*;

import java.lang.reflect.Type;

public class NodeValueTypeAdapter implements JsonDeserializer<NodeValue>, JsonSerializer<NodeValue> {

    @Override
    public NodeValue deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(jsonElement, SimpleNodeValue.class);
    }

    @Override
    public JsonElement serialize(NodeValue nodeValue, Type type, JsonSerializationContext context) {
        return context.serialize(nodeValue.getValue());
    }
}
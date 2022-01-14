package com.github.imthenico.simplecommons.data.mapper.gson;

import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TreeNodeTypeAdapter implements JsonDeserializer<TreeNode>, JsonSerializer<TreeNode> {

    @Override
    public TreeNode deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        TreeNode node = TreeNode.create();

        Map<String, Object> map = context.deserialize(jsonElement, Map.class);

        node.set(map);

        return node;
    }

    @Override
    public JsonElement serialize(TreeNode treeNode, Type type, JsonSerializationContext context) {
        return context.serialize(treeNode.simple());
    }
}
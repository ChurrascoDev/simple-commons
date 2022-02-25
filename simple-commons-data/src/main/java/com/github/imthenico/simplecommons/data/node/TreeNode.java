package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.data.node.value.SimpleNodeValue;
import com.github.imthenico.simplecommons.util.Validate;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public interface TreeNode {

    FindResult find(String targetPath);

    FindResult all();

    @Deprecated
    NodeValue get(String path);

    String getString(String path);

    int getInt(String path);

    int getDouble(String path);

    long getLong(String path);

    byte getByte(String path);

    float getFloat(String path);

    boolean getBoolean(String path);

    <T> List<T> getList(String path);

    void set(String path, Object value);

    void set(Map<String, Object> values);

    Optional<TreeNode> getNode(String path);

    Optional<TreeNode> createNode(String path);

    TreeNode getOrCreate(String path);

    TreeNode parent();

    TreeNode root();

    boolean mutable();

    Map<String, NodeValue> simple();

    Set<String> keys();

    default void forEach(BiConsumer<String, NodeValue> action, boolean deep) {
        for (Map.Entry<String, NodeValue> entry : simple().entrySet()) {
            NodeValue value = Validate.defIfNull(entry.getValue(), SimpleNodeValue.EMPTY);

            Optional<TreeNode> possibleNode = value.getAsNode();

            if (possibleNode.isPresent() && deep) {
                possibleNode.get().forEach(action, true);
            }

            action.accept(entry.getKey(), value);
        }
    }

    static TreeNode create() {
        return new SimpleTreeNode(null, null);
    }

    static TreeNode copy(TreeNode treeNode) {
        TreeNode copy = create();

        treeNode.forEach(copy::set, false);

        return copy;
    }

    static AdapterNode createAdapterNode(GenericMapper<String> mapper) {
        return new AdapterNode(create(), mapper);
    }

    static AdapterNode adapterNode(GenericMapper<String> mapper, TreeNode delegate) {
        return new AdapterNode(delegate, mapper);
    }

    static TreeNode load(
            GenericMapper<String> mapper,
            File file
    ) throws IOException {
        return load(mapper, new FileReader(file));
    }

    static TreeNode load(
            GenericMapper<String> mapper,
            Reader reader
    ) throws IOException {
       try (BufferedReader bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
           StringBuilder stringBuilder = new StringBuilder();

           String line;

           while ((line = bufferedReader.readLine()) != null) {
               stringBuilder.append(line);
           }

           return mapper.map(stringBuilder.toString(), TreeNode.class);
       }
    }
}
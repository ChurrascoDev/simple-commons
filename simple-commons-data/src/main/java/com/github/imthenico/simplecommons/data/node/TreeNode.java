package com.github.imthenico.simplecommons.data.node;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public interface TreeNode {

    NodeValue get(String path);

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
            Optional<TreeNode> possibleNode = entry.getValue().getAsNode();

            if (possibleNode.isPresent() && deep) {
                possibleNode.get().forEach(action, true);
            }

            action.accept(entry.getKey(), entry.getValue());
        }
    }
}
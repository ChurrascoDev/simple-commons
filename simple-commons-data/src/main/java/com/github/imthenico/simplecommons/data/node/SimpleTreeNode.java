package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.util.Validate;
import com.github.imthenico.simplecommons.util.list.ArrayContainer;
import com.github.imthenico.simplecommons.value.*;

import java.util.*;

public class SimpleTreeNode implements TreeNode {

    private final Map<String, NodeValue> valueMap;
    private final TreeNode parent;
    private final TreeNode root;

    public SimpleTreeNode(
            TreeNode parent,
            TreeNode root
    ) {
        this.parent = parent;
        this.root = root;
        this.valueMap = new HashMap<>();
    }

    @Override
    public NodeValue get(String path) {
        int i = path.lastIndexOf(".");

        NodeValue value = null;
        if (i != -1) {
            String lastPathKey = path.substring(i + 1);
            String nodePath = path.substring(0, i);

            Optional<TreeNode> possibleNode = get(nodePath).getAsNode();

            if (possibleNode.isPresent()) {
                TreeNode node = possibleNode.get();

                value = node.get(lastPathKey);
            }
        } else {
            value = valueMap.get(path);
        }

        return Validate.defIfNull(value, new EmptyNodeValue());
    }

    @Override
    public void set(String path, Object value) {
        int i = path.lastIndexOf(".");

        if (i != -1) {
            String lastPathKey = path.substring(i + 1);
            String nodePath = path.substring(0, i);

            getOrCreate(nodePath).set(lastPathKey, value);
        } else {
            internalValueCreation(path, value);
        }
    }

    @Override
    public void set(Map<String, Object> values) {
        values.forEach(this::set);
    }

    @Override
    public Optional<TreeNode> getNode(String path) {
        return get(path).getAsNode();
    }

    @Override
    public Optional<TreeNode> createNode(String path) {
        int i = path.lastIndexOf(".");

        if (i != -1) {
            String lastPathKey = path.substring(i + 1);
            String nodePath = path.substring(0, i);
            TreeNode temp = this;

            Optional<TreeNode> possibleNode = temp.createNode(nodePath);

            if (possibleNode.isPresent()) {
                temp = possibleNode.get();
            }

            return temp != this ? temp.createNode(lastPathKey) : Optional.empty();
        } else {
            if (getNode(path).isPresent()) {
                return Optional.empty();
            }

            TreeNode node = new SimpleTreeNode(this, root);
            internalValueCreation(path, node);

            return Optional.of(node);
        }
    }

    @Override
    public TreeNode getOrCreate(String path) {
        Optional<TreeNode> possibleNode = getNode(path);

        if (!possibleNode.isPresent()) {
            possibleNode = createNode(path);
        }

        return possibleNode.orElse(this);
    }

    @Override
    public TreeNode parent() {
        return parent;
    }

    @Override
    public TreeNode root() {
        return Validate.defIfNull(root, this);
    }

    @Override
    public boolean mutable() {
        return true;
    }

    @Override
    public Map<String, NodeValue> simple() {
        return Collections.unmodifiableMap(valueMap);
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(valueMap.keySet());
    }

    private void internalValueCreation(String key, Object value) {
        valueMap.compute(key, (k, v) -> new SimpleNodeValue(value));
    }

    class SimpleNodeValue extends SimpleAbstractValue implements NodeValue {

        public SimpleNodeValue(Object value) {
            super(null);

            if (!(value instanceof TreeNode)) {
                checkType(value);
            }

            this.value = value;
        }

        @Override
        public Optional<TreeNode> getAsNode() {
            return value instanceof TreeNode ? Optional.of((TreeNode) value) : Optional.empty();
        }

        @Override
        public NodeValue immutableCopy() {
            return new SimpleNodeValue(value);
        }

        @Override
        public boolean mutable() {
            return SimpleTreeNode.this.mutable();
        }
    }

    static class EmptyNodeValue implements NodeValue {

        @Override
        public Optional<TreeNode> getAsNode() {
            return Optional.empty();
        }

        @Override
        public NodeValue immutableCopy() {
            return this;
        }

        @Override
        public <T> T getValue() {
            return null;
        }

        @Override
        public boolean mutable() {
            return false;
        }

        @Override
        public MutableValue mutableCopy() {
            return new SimpleMutableValue(null);
        }

        @Override
        public <T> Optional<T> cast() {
            return Optional.empty();
        }

        @Override
        public <T> Optional<T> cast(Class<T> clazz) {
            return Optional.empty();
        }

        @Override
        public Optional<String> getAsString() {
            return Optional.empty();
        }

        @Override
        public Optional<Character> getAsChar() {
            return Optional.empty();
        }

        @Override
        public Optional<Number> getAsNumber() {
            return Optional.empty();
        }

        @Override
        public Optional<Boolean> getAsBoolean() {
            return Optional.empty();
        }

        @Override
        public <E> Optional<ArrayContainer<E>> getAsArray() {
            return Optional.empty();
        }
    }
}
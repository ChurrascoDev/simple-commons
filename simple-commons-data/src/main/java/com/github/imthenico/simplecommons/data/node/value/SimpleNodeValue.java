package com.github.imthenico.simplecommons.data.node.value;

import com.github.imthenico.simplecommons.data.exception.IllegalValueTypeException;
import com.github.imthenico.simplecommons.data.node.NodeValue;
import com.github.imthenico.simplecommons.data.node.NodeValueList;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.util.list.CustomList;
import com.github.imthenico.simplecommons.value.AbstractValue;

import java.util.Objects;
import java.util.Optional;

public class SimpleNodeValue implements NodeValue {

    public static final SimpleNodeValue EMPTY = new SimpleNodeValue(null);

    private final Object value;

    public SimpleNodeValue(Object value) {
        if (value != null) {
            if (value instanceof TreeNode || value instanceof NodeValueList || value instanceof AbstractValue) {
                this.value = value;
                return;
            }

            throw new IllegalValueTypeException(value);
        }

        this.value = null;
    }

    @Override
    public Optional<TreeNode> getAsNode() {
        return value instanceof TreeNode ? Optional.of((TreeNode) value) : Optional.empty();
    }

    @Override
    public Optional<NodeValueList> getAsArray() {
        return value instanceof CustomList ? Optional.of((NodeValueList) value) : Optional.empty();
    }

    @Override
    public Optional<AbstractValue> getAsSimpleValue() {
        return value instanceof AbstractValue ? Optional.of((AbstractValue) value) : Optional.empty();
    }

    @Override
    public NodeValue immutableCopy() {
        return new SimpleNodeValue(value);
    }

    @Override
    public boolean isNull() {
        return value == null;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }
}
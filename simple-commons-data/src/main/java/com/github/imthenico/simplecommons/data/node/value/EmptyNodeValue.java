package com.github.imthenico.simplecommons.data.node.value;

import com.github.imthenico.simplecommons.data.node.NodeValue;
import com.github.imthenico.simplecommons.data.node.NodeValueList;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.value.AbstractValue;

import java.util.Optional;

public class EmptyNodeValue implements NodeValue {

    public final static EmptyNodeValue INSTANCE = new EmptyNodeValue();

    private EmptyNodeValue() {}

    @Override
    public Optional<TreeNode> getAsNode() {
        return Optional.empty();
    }

    @Override
    public Optional<NodeValueList> getAsArray() {
        return Optional.empty();
    }

    @Override
    public Optional<AbstractValue> getAsSimpleValue() {
        return Optional.empty();
    }

    @Override
    public NodeValue immutableCopy() {
        return null;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Object getValue() {
        return null;
    }
}
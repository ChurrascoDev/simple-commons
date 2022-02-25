package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.value.AbstractValue;

import java.util.Optional;

@Deprecated
public interface NodeValue {

    Optional<TreeNode> getAsNode();

    Optional<NodeValueList> getAsArray();

    Optional<AbstractValue> getAsSimpleValue();

    NodeValue immutableCopy();

    boolean isNull();

    Object getValue();

}
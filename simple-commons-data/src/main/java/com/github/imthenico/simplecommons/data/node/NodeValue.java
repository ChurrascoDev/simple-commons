package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.value.AbstractValue;
import com.github.imthenico.simplecommons.value.MutableValue;

import java.util.Optional;

public interface NodeValue extends AbstractValue {

    Optional<TreeNode> getAsNode();

    @Override
    NodeValue immutableCopy();

    @Override
    MutableValue mutableCopy();

}
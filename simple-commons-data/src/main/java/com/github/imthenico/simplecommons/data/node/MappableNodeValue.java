package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.data.exception.MappingException;
import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.data.mapper.Mappable;
import com.github.imthenico.simplecommons.util.Validate;
import com.github.imthenico.simplecommons.value.AbstractValue;

import java.util.*;

public class MappableNodeValue implements Mappable, NodeValue {

    private final GenericMapper<String> mapper;
    private final NodeValue value;

    public MappableNodeValue(NodeValue value, GenericMapper<String> mapper) {
        this.mapper = Validate.notNull(mapper);
        this.value = Validate.notNull(value);
    }

    @Override
    public <T> T map(Class<T> target) throws MappingException {
        if (isNull())
            return null;

        return mapValue(mapper, value, Validate.notNull(target));
    }

    public <T> List<T> mapAll(Class<T> target) throws MappingException {
        if (isNull())
            return Collections.emptyList();

        Optional<NodeValueList> optionalNodeValues = value.getAsArray();

        if (optionalNodeValues.isPresent()) {
            NodeValueList nodeValues = optionalNodeValues.get();
            List<T> list = new ArrayList<>(nodeValues.size());

            for (NodeValue nodeValue : nodeValues) {
                T mapped = mapValue(mapper, nodeValue, Validate.notNull(target));

                if (mapped != null)
                    list.add(mapped);
            }

            return list;
        } else {
            return Collections.singletonList(map(target));
        }
    }

    @Override
    public Optional<TreeNode> getAsNode() {
        return value.getAsNode();
    }

    @Override
    public Optional<NodeValueList> getAsArray() {
        return value.getAsArray();
    }

    @Override
    public Optional<AbstractValue> getAsSimpleValue() {
        return value.getAsSimpleValue();
    }

    @Override
    public NodeValue immutableCopy() {
        return value.immutableCopy();
    }

    @Override
    public boolean isNull() {
        return value.isNull();
    }

    public NodeValue getValue() {
        return value;
    }

    private static <T> T mapValue(GenericMapper<String> mapper, NodeValue value, Class<T> target) {
        Object toSerialize = null;

        Optional<AbstractValue> optionalValue = value.getAsSimpleValue();
        Optional<TreeNode> optionalNode = value.getAsNode();

        if (optionalValue.isPresent()) {
            toSerialize = optionalValue.get();
        } else if (optionalNode.isPresent()) {
            toSerialize = optionalNode.get();
        }

        if (toSerialize != null) {
            return mapper.mapDirectly(toSerialize, target);
        }

        return null;
    }
}
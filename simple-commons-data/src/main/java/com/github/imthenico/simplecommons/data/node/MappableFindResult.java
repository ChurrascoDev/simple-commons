package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.data.mapper.GenericMapper;

import java.util.LinkedList;
import java.util.List;

public class MappableFindResult extends FindResultImpl {

    private final GenericMapper<?> mapper;

    MappableFindResult(List<NodeValue> foundElements, GenericMapper<?> mapper) {
        super(foundElements);
        this.mapper = mapper;
    }

    public <T> T map(int index, Class<T> target) {
        return mapper.mapDirectly(index, target);
    }

    public <T> List<T> mapAll(Class<T> target) {
        List<T> list = new LinkedList<>();

        for (Object obj : toList()) {
            T serialized = mapper.mapDirectly(obj, target);

            if (serialized == null)
                continue;

            list.add(serialized);
        }

        return list;
    }
}
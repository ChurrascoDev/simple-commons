package com.github.imthenico.simplecommons.data.key;

import com.github.imthenico.simplecommons.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class SimpleSourceKey implements SourceKey {

    private final Object key;
    private final Map<String, Object> extraData;

    public SimpleSourceKey(
            Object key,
            Object... extraData
    ) {
        this.key = key;
        this.extraData = new HashMap<>();

        for (Pair<Object, Object> pair : Pair.of(extraData)) {
            this.extraData.put((String) pair.getLeft(), pair.getRight());
        }
    }

    @Override
    public <T> T getKey() {
        return (T) key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E getExtraData(String dataKey, E def) {
        return (E) extraData.getOrDefault(dataKey, def);
    }
}
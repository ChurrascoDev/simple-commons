package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.util.Pair;
import com.github.imthenico.simplecommons.util.Validate;
import com.github.imthenico.simplecommons.value.AbstractValue;
import com.github.imthenico.simplecommons.value.SimpleAbstractValue;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class StatementValuePacket {

    private final Map<AbstractValue, Object> values;

    private StatementValuePacket(Map<AbstractValue, Object> values) {
        this.values = values;
    }

    public Map<AbstractValue, Object> getValues() {
        return Collections.unmodifiableMap(values);
    }

    public StatementValuePacket bind(Object key, Object value) {
        AbstractValue keyValue;
        if (key instanceof AbstractValue) {
            keyValue = (AbstractValue) key;
        } else {
            keyValue = new SimpleAbstractValue(key);
        }

        Validate.isTrue(keyValue.getAsString().isPresent() || keyValue.getAsNumber().isPresent(), "Invalid parameter key type");

        this.values.put(keyValue, value);
        return this;
    }

    public StatementValuePacket bind(Map<?, ?> objectMap) {
        objectMap.forEach(this::bind);
        return this;
    }

    public static StatementValuePacket ofMap(Map<?, ?> objectMap) {
        return new StatementValuePacket(new LinkedHashMap<>(objectMap.size())).bind(objectMap);
    }


    public static StatementValuePacket of(Object... objects) {
        return of(Pair.of(true, objects));
    }

    public static StatementValuePacket of(Pair<Object, Object>[] pairs) {
        StatementValuePacket packet = new StatementValuePacket(new LinkedHashMap<>(pairs.length));
        for (Pair<Object, Object> pair : pairs) {
            packet.bind(pair.getLeft(), pair.getRight());
        }

        return packet;
    }
}
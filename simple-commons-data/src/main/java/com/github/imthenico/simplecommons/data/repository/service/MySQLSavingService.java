package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.repository.exception.SerializationException;

import java.util.Map;


public class MySQLSavingService<T> extends AbstractSQLSavingService<T> {

    @Override
    public void save(T obj, String key) {
        try {
            Map<String, Object> serializedObj = mapper.serializeFields(key);

            processor.newBinder("REPLACE INTO <table> VALUES(<k>,<p>)")
                    .key(key)
                    .parameters(sqlTableModel.toParameters(false))
                    .bindValues(serializedObj)
                    .update();
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }
}
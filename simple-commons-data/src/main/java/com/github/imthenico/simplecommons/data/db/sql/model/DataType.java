package com.github.imthenico.simplecommons.data.db.sql.model;

import com.github.imthenico.simplecommons.util.Validate;

public class DataType {

    private final String name;
    private final int dataLength;

    public DataType(String name, int dataLength) {
        this.name = Validate.notNull(name);
        this.dataLength = dataLength;
    }

    public DataType(String name) {
        this(name, -1);
    }

    public String getName() {
        return name;
    }

    public int getDataLength() {
        return dataLength;
    }
}
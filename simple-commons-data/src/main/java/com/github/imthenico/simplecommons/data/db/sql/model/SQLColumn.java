package com.github.imthenico.simplecommons.data.db.sql.model;

public class SQLColumn {

    private final String name;
    private final String dataType;
    private final boolean primary;
    private final String asParameter;

    SQLColumn(String name, String dataType, boolean primary) {
        this.name = name;
        this.dataType = dataType;
        this.primary = primary;
        this.asParameter = name + "=?";
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String toParameter() {
        return asParameter;
    }

    public boolean isPrimary() {
        return primary;
    }
}
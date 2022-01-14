package com.github.imthenico.simplecommons.data.db.sql.model;

import com.github.imthenico.simplecommons.util.Validate;

public class SQLColumn {

    private final String name;
    private final DataType dataType;
    private final String asParameter;
    private final Object defaultValue;
    private final boolean autoIncrement;
    private final Constraint constraint;
    private final String statementPart;

    SQLColumn(String name, DataType dataType, Object defaultValue, boolean autoIncrement, Constraint constraint) {
        this.name = name;
        this.dataType = Validate.notNull(dataType);
        this.asParameter = name + "=?";
        this.defaultValue = defaultValue;
        this.autoIncrement = autoIncrement;
        this.constraint = constraint;
        this.statementPart = toStatementPart();
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String toParameter() {
        return asParameter;
    }

    public Object defaultValue() {
        return defaultValue;
    }

    public boolean autoIncrement() {
        return autoIncrement;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public String getStatementPart() {
        return statementPart;
    }

    private String toStatementPart() {
        StringBuilder builder = new StringBuilder();

        builder.append(name)
                .append(" ")
                .append(dataType.getName());

        if (dataType.getDataLength() > -1) {
            builder.append("(").append(dataType.getDataLength()).append(")");
        }

        if (constraint != null) {
            builder.append(" ").append(constraint.getStatementPart());
        }

        if (defaultValue != null) {
            builder.append(String.format(" DEFAULT %s", defaultValue));
        }

        if (autoIncrement) {
            builder.append("AUTO_INCREMENT");
        }

        return builder.toString();
    }
}
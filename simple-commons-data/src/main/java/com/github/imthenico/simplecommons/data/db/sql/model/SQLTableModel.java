package com.github.imthenico.simplecommons.data.db.sql.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SQLTableModel {

    private final String name;
    private final List<SQLColumn> columns;
    private final SQLColumn primaryColumn;
    private final List<String> columnNames;

    SQLTableModel(String name, SQLColumn primaryColumn, SQLColumn... columns) {
        this.name = name;
        this.primaryColumn = primaryColumn;
        this.columns = new ArrayList<>();

        this.columns.addAll(Arrays.asList(columns));

        this.columnNames = new ArrayList<>();
        for (SQLColumn column : this.columns) {
            this.columnNames.add(column.getName());
        }
    }

    public String getName() {
        return name;
    }

    public List<SQLColumn> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public SQLColumn getPrimaryColumn() {
        return primaryColumn;
    }

    public String toParameters(boolean includeNames) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            SQLColumn column = columns.get(i);

            if (includeNames) {
                builder.append(column.toParameter());
            } else {
                builder.append("?");
            }

            if (i < (columns.size() - 1)) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public List<String> getColumnNames() {
        return Collections.unmodifiableList(columnNames);
    }

    public static final class Builder {
        private final List<SQLColumn> columns;
        private final String name;
        private SQLColumn primaryColumn;

        public Builder(String name) {
            this.name = name;
            this.columns = new ArrayList<>();
        }

        public Builder column(String name, String dataType) {
            this.columns.add(new SQLColumn(name, dataType, false));
            return this;
        }

        public Builder primary(String name, String dataType) {
            this.primaryColumn = new SQLColumn(name, dataType, true);
            return this;
        }

        public SQLTableModel build() {
            return new SQLTableModel(name, primaryColumn, columns.toArray(columns.toArray(new SQLColumn[0])));
        }
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }
}
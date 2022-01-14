package com.github.imthenico.simplecommons.data.db.sql.query.impl;

import com.github.imthenico.simplecommons.data.db.sql.query.QueryPart;
import com.github.imthenico.simplecommons.data.db.sql.query.StatementValueBinder;
import com.github.imthenico.simplecommons.data.db.sql.query.StatementValuePacket;
import com.github.imthenico.simplecommons.data.exception.IllegalValueTypeException;
import com.github.imthenico.simplecommons.util.Validate;
import com.github.imthenico.simplecommons.value.AbstractValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StatementValueBinderImpl implements StatementValueBinder {

    private final PreparedStatement statement;
    private final List<QueryPart> parts;
    private final Map<String, Integer> columns;

    StatementValueBinderImpl(
            PreparedStatement statement,
            QueryBuilderImpl queryBuilder
    ) throws SQLException {
        this.statement = statement;
        this.parts = queryBuilder.parts;
        this.columns = new HashMap<>();

        Connection connection = statement.getConnection();
        try (ResultSet resultSet = connection.getMetaData().getColumns(null, null, queryBuilder.table, null)) {
            while (resultSet.next()) {
                columns.put(resultSet.getString("COLUMN_NAME"), resultSet.getInt("ORDINAL_POSITION"));
            }
        }
    }

    @Override
    public StatementValueBinder set(String placeholder, Object value) {
        int indexOf = parts.indexOf(new QueryPart(placeholder));

        if (indexOf == -1) {
            indexOf = columns.getOrDefault(placeholder, -1);
        }

        Validate.isTrue(indexOf != -1, String.format("No such index for placeholder '%s'", placeholder));

        return set(indexOf, value);
    }

    @Override
    public StatementValueBinder set(int index, Object value) {
        try {
            statement.setObject(Validate.isTrue(index > 0, "index <= 0", index), value);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public StatementValueBinder withMap(Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            this.set(entry.getKey(), entry.getValue());
        }

        return this;
    }

    @Override
    public StatementValueBinder open(StatementValuePacket packet) {
        Map<AbstractValue, Object> values = packet.getValues();

        values.forEach((k, v) -> {
            Optional<String> possibleString = k.getAsString();
            Optional<Number> possibleInt = k.getAsNumber();

            if (possibleString.isPresent()) {
                set(possibleString.get(), v);
            } else if (possibleInt.isPresent()){
                set(possibleInt.get().intValue(), v);
            } else {
                Object value = k.getValue();

                throw new IllegalValueTypeException(value);
            }
        });
        return this;
    }

    @Override
    public PreparedStatement get() {
        return statement;
    }

    @Override
    public void close() throws SQLException {
        statement.close();
    }
}
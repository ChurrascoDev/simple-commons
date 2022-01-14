package com.github.imthenico.simplecommons.data.db.sql.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public interface StatementValueBinder extends AutoCloseable {

    StatementValueBinder set(
            String placeholder,
            Object value
    );

    StatementValueBinder set(
            int index,
            Object value
    );

    StatementValueBinder withMap(
            Map<String, Object> values
    );

    StatementValueBinder open(
            StatementValuePacket packet
    );

    PreparedStatement get();

    @Override
    void close() throws SQLException;
}
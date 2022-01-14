package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.data.db.sql.model.Constraint;
import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.iterator.UnmodifiableIterator;
import com.github.imthenico.simplecommons.util.Validate;
import com.github.imthenico.simplecommons.value.AbstractValue;
import com.github.imthenico.simplecommons.value.SimpleAbstractValue;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class QueryResult implements Iterable<Map<String, AbstractValue>>{

    public static final QueryResult EMPTY = new QueryResult();

    private final Map<String, Map<String, AbstractValue>> allResults;
    private final List<String> identifiers;

    private QueryResult() {
        this.allResults = Collections.emptyMap();
        this.identifiers = Collections.emptyList();

    }

    private QueryResult(ResultSet resultSet, SQLTableModel sqlTableModel) throws SQLException {
        this.allResults = new HashMap<>();
        this.identifiers = new ArrayList<>(allResults.keySet());

        ResultSetMetaData metaData = resultSet.getMetaData();
        int index = 0;
        while (resultSet.next()) {
            String id;

            if (sqlTableModel != null) {
                id = resultSet.getObject(sqlTableModel.filterByConstraint(Constraint.PRIMARY).getName()).toString();
            } else {
                id = Objects.toString(index);
            }

            Map<String, AbstractValue> row = new HashMap<>();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object result = resultSet.getObject(i);

                if (result instanceof Array) {
                    result = ((Array) result).getArray();
                }

                AbstractValue value = new SimpleAbstractValue(result);

                row.put(columnName, value);
            }

            allResults.put(id, row);

            index++;
        }
    }

    public Map<String, AbstractValue> getRow(String id) {
        return allResults.get(id);
    }

    public List<String> getAllIdentifiers() {
        return identifiers;
    }

    @Override
    public Iterator<Map<String, AbstractValue>> iterator() {
        return new UnmodifiableIterator<>(allResults.values().iterator());
    }

    public static QueryResult parse(ResultSet resultSet, SQLTableModel tableModel) throws SQLException {
        Validate.isTrue(!resultSet.isClosed(), "Closed ResultSet.");

        if (!resultSet.isBeforeFirst()) {
            return EMPTY;
        }

        return new QueryResult(resultSet, tableModel);
    }
}
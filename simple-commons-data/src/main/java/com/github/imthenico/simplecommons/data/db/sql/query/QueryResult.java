package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.data.db.sql.model.Constraint;
import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.iterator.UnmodifiableIterator;
import com.github.imthenico.simplecommons.object.BasicTypeObject;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class QueryResult implements Iterable<Map<String, BasicTypeObject>>{

    public static final QueryResult EMPTY = new QueryResult();

    private final Map<String, Map<String, BasicTypeObject>> allResults;

    private QueryResult() {
        this.allResults = Collections.emptyMap();
    }

    public QueryResult(ResultSet resultSet, SQLTableModel sqlTableModel) throws SQLException {
        this.allResults = new HashMap<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            String id = resultSet.getObject(sqlTableModel.filterByConstraint(Constraint.PRIMARY).getName()).toString();
            Map<String, BasicTypeObject> row = new HashMap<>();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object result = resultSet.getObject(i);

                if (result instanceof Array) {
                    result = ((Array) result).getArray();
                }

                BasicTypeObject basicTypeObject = new BasicTypeObject(result);

                row.put(columnName, basicTypeObject);
            }

            allResults.put(id, row);
        }
    }

    public Map<String, BasicTypeObject> getRow(String id) {
        return allResults.get(id);
    }

    public Set<String> getAllIdentifiers() {
        return allResults.keySet();
    }

    @Override
    public Iterator<Map<String, BasicTypeObject>> iterator() {
        return new UnmodifiableIterator<>(allResults.values().iterator());
    }
}
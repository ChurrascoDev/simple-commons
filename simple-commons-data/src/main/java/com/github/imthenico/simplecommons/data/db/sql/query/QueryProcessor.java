package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.util.Validate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryProcessor {

    private final Connection connection;
    private final SQLTableModel sqlTableModel;

    public QueryProcessor(Connection connection, SQLTableModel sqlTableModel) {
        this.connection = Validate.notNull(connection);
        this.sqlTableModel = Validate.notNull(sqlTableModel);

        try {
            getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String query) {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public QueryResult executeQuery(PreparedStatement statement) {
        try {
            Validate.isTrue(!statement.isClosed(), "closed statement");

            return new QueryResult(statement.executeQuery(), sqlTableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return QueryResult.EMPTY;
    }

    public QueryResult executeQuery(String query) {
        try {
            return executeQuery(connection.prepareStatement(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return QueryResult.EMPTY;
    }

    public void executeUpdate(PreparedStatement statement) {
        try {
            Validate.isTrue(!statement.isClosed(), "closed statement");

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeUpdate(String query) {
        try {
            executeUpdate(connection.prepareStatement(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ExecutableValueBinder newBinder(String query) {
        return new ExecutableValueBinder(query, connection, this);
    }

    public Connection getConnection() throws SQLException {
        if (connection.isClosed())
            throw new IllegalArgumentException("Connection closed");

        return connection;
    }

    public class ExecutableValueBinder {

        private final Map<Object, String> valuesToBind;
        private final Connection connection;
        private final QueryProcessor queryProcessor;

        private String query;

        public ExecutableValueBinder(String query, Connection connection, QueryProcessor queryProcessor) {
            this.connection = Validate.notNull(connection);
            this.queryProcessor = Validate.notNull(queryProcessor);
            this.query = Validate.notNull(query);
            this.valuesToBind = new HashMap<>();
        }

        public ExecutableValueBinder bindString(String toMatch, String replacement) {
            if (toMatch != null && replacement != null)
                query = query.replace(toMatch, replacement);
            return this;
        }

        public ExecutableValueBinder key(Object key) {
            return bindString("<k>", key.toString());
        }

        public ExecutableValueBinder parameters(String parameters) {
            this.query = query.replace("<p>", parameters);
            return this;
        }

        public ExecutableValueBinder parameters(String... parameters) {
            StringBuilder builder = new StringBuilder();

            for (String parameter : parameters) {
                builder.append(parameter);
            }

            return parameters(builder.toString());
        }

        public ExecutableValueBinder bindValue(String paramName, Object value) {
            valuesToBind.put(value, paramName);
            return this;
        }

        public ExecutableValueBinder bindValue(Object value) {
            valuesToBind.put(value, null);
            return this;
        }

        public ExecutableValueBinder bindValues(Map<String, Object> objectMap) {
            objectMap.forEach((k, v) -> valuesToBind.put(v, k));
            return this;
        }

        public ExecutableValueBinder bindValues(Object... objects) {
            for (Object object : objects) {
                bindValue(object);
            }
            return this;
        }

        public ExecutableValueBinder bindValues(Collection<Object> objects) {
            for (Object object : objects) {
                bindValue(object);
            }
            return this;
        }

        public void update() {
            try (PreparedStatement statement = prepareStatement()) {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public QueryResult query() {
            try (PreparedStatement statement = prepareStatement()) {
                return queryProcessor.executeQuery(statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return QueryResult.EMPTY;
        }

        private PreparedStatement prepareStatement() throws SQLException {
            bindString("<table>", sqlTableModel.getName());

            List<String> columnNames = sqlTableModel.getColumnNames();
            PreparedStatement statement = connection.prepareStatement(query);

            int i = 1;
            for (Map.Entry<Object, String> entry : valuesToBind.entrySet()) {
                String paramName = entry.getValue();

                int paramIndex = paramName != null ? columnNames.indexOf(paramName) + 1 : i;

                statement.setObject(paramIndex, entry.getKey());
                i++;
            }

            return statement;
        }
    }
}
package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.util.Validate;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcessor {

    private final static Pattern PARAMETER_PATTERN = Pattern.compile("\\w+");

    private final Connection connection;
    private final SQLTableModel sqlTableModel;

    public QueryProcessor(Connection connection, SQLTableModel sqlTableModel) {
        this.connection = Validate.notNull(connection);
        this.sqlTableModel = sqlTableModel;

        try {
            getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public QueryProcessor(Connection connection) {
        this(connection, null);
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

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return QueryResult.EMPTY;
            }

            return new QueryResult(resultSet, sqlTableModel);
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

    public int executeUpdate(PreparedStatement statement) {
        try {
            Validate.isTrue(!statement.isClosed(), "closed statement");

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public int executeUpdate(String query) {
        try {
            return executeUpdate(connection.prepareStatement(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public ExecutableValueBinder newBinder(String query) {
        return new ExecutableValueBinder(query, connection, this);
    }

    public Connection getConnection() throws SQLException {
        if (connection.isClosed())
            throw new IllegalArgumentException("Connection closed");

        return connection;
    }

    public static class ExecutableValueBinder {

        private final Map<Object, Object> valuesToBind;
        private final Connection connection;
        private final QueryProcessor queryProcessor;
        private final SQLTableModel sqlTableModel;

        private String query;
        private String parameters;

        public ExecutableValueBinder(String query, Connection connection, QueryProcessor queryProcessor) {
            this.connection = Validate.notNull(connection);
            this.queryProcessor = Validate.notNull(queryProcessor);
            this.query = Validate.notNull(query);
            this.sqlTableModel = queryProcessor.sqlTableModel;
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
            this.parameters = Validate.notNull(parameters);
            return this;
        }

        public ExecutableValueBinder parameters(String... parameters) {
            StringBuilder builder = new StringBuilder();

            for (String parameter : parameters) {
                builder.append(parameter);
            }

            return parameters(builder.toString());
        }

        public ExecutableValueBinder bindValue(Object param, Object value) {
            valuesToBind.put(value, param);
            return this;
        }

        public ExecutableValueBinder bindValue(Object value) {
            valuesToBind.put(value, null);
            return this;
        }

        public ExecutableValueBinder bindValues(Map<Object, Object> objectMap) {
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

        public int update() {
            try (PreparedStatement statement = prepareStatement()) {
                return statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;
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
            List<String> columnNames = new ArrayList<>();

            if (sqlTableModel != null) {
                bindString("<table>", sqlTableModel.getName());
                columnNames.addAll(sqlTableModel.getColumnNames());
            }

            if (parameters != null) {
                query = query.replace("<p>", parameters);
                Matcher matcher = PARAMETER_PATTERN.matcher(parameters);

                columnNames.clear();
                while (matcher.find()) {
                    columnNames.add(matcher.group());
                }
            }

            PreparedStatement statement = connection.prepareStatement(query);

            int i = 1;
            for (Map.Entry<Object, Object> entry : valuesToBind.entrySet()) {
                Object param = entry.getValue();
                int paramIndex = i;

                if (param instanceof String) {
                    String paramName = (String) param;
                    paramIndex = columnNames.indexOf(paramName) + 1;

                } else if (param instanceof Integer) {
                    paramIndex = (int) param;
                }

                statement.setObject(paramIndex, entry.getKey());
                i++;
            }

            return statement;
        }
    }
}
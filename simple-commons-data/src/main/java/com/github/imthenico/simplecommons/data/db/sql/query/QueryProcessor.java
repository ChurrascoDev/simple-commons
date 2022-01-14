package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.util.Validate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryProcessor {

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

    public QueryResult executeQuery(String query) {
        try {
            return executeQuery(connection.prepareStatement(query), sqlTableModel, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return QueryResult.EMPTY;
    }

    public int executeUpdate(String query) {
        try {
            return executeUpdate(connection.prepareStatement(query), true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Connection getConnection() throws SQLException {
        if (connection.isClosed())
            throw new IllegalArgumentException("Connection closed");

        return connection;
    }

    public static QueryResult executeQuery(PreparedStatement statement, SQLTableModel sqlTableModel, boolean closeStatement) {
        try {
            Validate.isTrue(!statement.isClosed(), "closed statement");

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return QueryResult.EMPTY;
            }

            return QueryResult.parse(resultSet, sqlTableModel);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (closeStatement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return QueryResult.EMPTY;
    }

    public static QueryResult executeQuery(PreparedStatement statement, boolean closeStatement) {
        return executeQuery(statement, null, closeStatement);
    }

    public static int executeUpdate(PreparedStatement statement, boolean closeStatement) {
        try {
            Validate.isTrue(!statement.isClosed(), "closed statement");

            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (closeStatement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return -1;
    }
}
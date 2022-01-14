package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.data.db.sql.query.impl.QueryBuilderImpl;

import java.sql.Connection;
import java.sql.SQLException;

public interface QueryBuilder {

    QueryBuilder table(String tableName);

    QueryBuilder addPlaceholder(String placeholder);

    QueryBuilder addPlaceholders(String... placeholders);

    QueryBuilder addParameters(int quantity);

    QueryBuilder replace(String toReplace, String replacement);

    QueryBuilder replace(String toReplace, QueryBuilder replacement);

    QueryBuilder useSchema(QuerySchema schema);

    String getQuery();

    StatementValueBinder prepare(Connection connection);

    static QueryBuilder create(String query) {
        return new QueryBuilderImpl(query);
    }
}
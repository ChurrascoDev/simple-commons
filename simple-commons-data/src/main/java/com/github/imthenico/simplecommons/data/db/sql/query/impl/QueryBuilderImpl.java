package com.github.imthenico.simplecommons.data.db.sql.query.impl;

import com.github.imthenico.simplecommons.data.db.sql.query.QueryBuilder;
import com.github.imthenico.simplecommons.data.db.sql.query.QueryPart;
import com.github.imthenico.simplecommons.data.db.sql.query.QuerySchema;
import com.github.imthenico.simplecommons.data.db.sql.query.StatementValueBinder;
import com.github.imthenico.simplecommons.util.Validate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilderImpl implements QueryBuilder {

    public final static QuerySchema DEFAULT_SCHEMA = new QuerySchema("<t>",  "<p>");

    protected final List<QueryPart> parts;
    private final Map<String, String> replacements;
    private final String query;
    private QuerySchema schema;
    protected String table;

    public QueryBuilderImpl(String query) {
        this.query = Validate.notNull(query);
        this.parts = new ArrayList<>();
        this.replacements = new HashMap<>();
    }

    @Override
    public QueryBuilder table(String tableName) {
        this.table = tableName;

        return this;
    }

    @Override
    public QueryBuilder addPlaceholder(String placeholder) {
        this.parts.add(QueryPart.placeholder(placeholder));

        return this;
    }

    @Override
    public QueryBuilder addPlaceholders(String... placeholders) {
        for (String placeholder : Validate.notNull(placeholders)) {
            addPlaceholder(placeholder);
        }

        return this;
    }

    @Override
    public QueryBuilder addParameters(int quantity) {
        for (int i = 0; i < quantity; i++) {
            parts.add(QueryPart.parameter());
        }

        return this;
    }

    @Override
    public QueryBuilder replace(String toReplace, String replacement) {
        this.replacements.put(Validate.notNull(toReplace), Validate.notNull(replacement));

        return this;
    }

    @Override
    public QueryBuilder replace(String toReplace, QueryBuilder replacement) {
        return replace(toReplace, replacement.getQuery());
    }

    @Override
    public QueryBuilder useSchema(QuerySchema schema) {
        this.schema = schema;

        return this;
    }

    @Override
    public String getQuery() {
        String query = this.query;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < parts.size(); i++) {
            String part = parts.get(i).getPart();
            builder.append(part);

            if (i < (parts.size() - 1)) {
                builder.append(", ");
            }
        }

        QuerySchema schema = Validate.defIfNull(this.schema, DEFAULT_SCHEMA);

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            query = query.replace(entry.getKey(), entry.getValue());
        }

        return query
                .replace(
                        schema.getTablePlaceholder(), Validate.defIfNull(table, "")
                )
                .replace(
                        schema.getParametersPlaceholder(), builder.toString()
                );
    }

    @Override
    public StatementValueBinder prepare(Connection connection) {
        try {
            return new StatementValueBinderImpl(connection.prepareStatement(getQuery()), this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
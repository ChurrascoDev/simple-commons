package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.Objects;

public class QueryPart {

    private final String part;

    public QueryPart(String part) {
        this.part = Validate.notNull(part);
    }

    public String getPart() {
        return part;
    }

    public static QueryPart parameter() {
        return new QueryPart("?");
    }

    public static QueryPart placeholder(String name) {
        return new QueryPart(name + "=?");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryPart queryPart = (QueryPart) o;
        return part.equals(queryPart.part);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(part);
    }
}
package com.github.imthenico.simplecommons.data.db.sql.model;

public enum Constraint {

    NOTNULL("NOT NULL"),
    PRIMARY("PRIMARY KEY"),
    UNIQUE("UNIQUE");

    private final String part;

    Constraint(String part) {
        this.part = part;
    }

    public String getStatementPart() {
        return part;
    }
}
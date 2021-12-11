package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.data.db.sql.query.QueryProcessor;
import com.github.imthenico.simplecommons.data.repository.GenericMapper;
import com.github.imthenico.simplecommons.data.repository.Response;
import com.github.imthenico.simplecommons.util.Validate;

import java.sql.Connection;

public abstract class AbstractSQLSavingService<T> implements SavingService<T> {

    private boolean initialized;

    protected SQLTableModel sqlTableModel;
    protected GenericMapper<?> mapper;
    protected QueryProcessor processor;

    public void init(SQLTableModel sqlTableModel, GenericMapper<?> mapper, Connection connection) {
        Validate.isTrue(!initialized, "sql saving service is already initialized");

        this.initialized = true;

        this.sqlTableModel = Validate.notNull(sqlTableModel);
        this.mapper = Validate.notNull(mapper);
        this.processor = new QueryProcessor(Validate.notNull(connection), sqlTableModel);
    }

    @Override
    public Response<?> asyncSave(T obj, String key) {
        return null;
    }
}
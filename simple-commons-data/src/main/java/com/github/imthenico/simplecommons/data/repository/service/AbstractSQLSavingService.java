package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.util.Validate;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractSQLSavingService<T> implements SavingService<T> {

    private boolean initialized;

    protected SQLTableModel sqlTableModel;
    protected GenericMapper<?> mapper;
    protected Connection connection;

    public void init(SQLTableModel sqlTableModel, GenericMapper<?> mapper, Connection connection) {
        Validate.isTrue(!initialized, "sql saving service is already initialized");

        this.initialized = true;

        this.sqlTableModel = Validate.notNull(sqlTableModel);
        this.mapper = Validate.notNull(mapper);
        this.connection = Validate.notNull(connection);
    }

    @Override
    public CompletableFuture<?> asyncSave(T obj, SourceKey key) {
        return null;
    }
}
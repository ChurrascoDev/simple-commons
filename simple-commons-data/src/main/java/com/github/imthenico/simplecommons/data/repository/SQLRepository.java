package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.data.db.sql.query.QueryProcessor;
import com.github.imthenico.simplecommons.data.db.sql.query.QueryResult;
import com.github.imthenico.simplecommons.data.repository.service.AbstractSQLSavingService;
import com.github.imthenico.simplecommons.data.repository.service.MySQLSavingService;
import com.github.imthenico.simplecommons.data.repository.service.SavingService;
import com.github.imthenico.simplecommons.object.BasicTypeObject;
import com.github.imthenico.simplecommons.util.Validate;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class SQLRepository<T> extends AbstractRepository<T> {

    private final QueryProcessor processor;
    private final SQLTableModel sqlTableModel;
    private final SavingService<T> delegatedSaving;
    private final GenericMapper<?> mapper;

    public SQLRepository(
            Consumer<Throwable> exceptionHandler,
            Executor taskProcessor,
            Class<T> modelClass,
            GenericMapper<?> mapper,
            Connection connection,
            SQLTableModel sqlTableModel,
            AbstractSQLSavingService<T> delegatedSaving
    ) {
        super(taskProcessor, modelClass);
        this.sqlTableModel = Validate.notNull(sqlTableModel);
        this.processor = new QueryProcessor(Validate.notNull(connection), sqlTableModel);
        this.delegatedSaving = Validate.notNull(delegatedSaving);
        this.mapper = Validate.notNull(mapper);
        delegatedSaving.init(sqlTableModel, mapper, connection);
    }

    public SQLRepository(
            Executor taskProcessor,
            Class<T> modelClass,
            GenericMapper<?> genericMapper,
            Connection connection,
            SQLTableModel sqlTableModel
    ) {
       this(null, taskProcessor, modelClass, genericMapper, connection, sqlTableModel, new MySQLSavingService<>());
    }

    @Override
    public Response<?> save(T obj, String key) {
        return run(() -> delegatedSaving.save(obj, key));
    }

    @Override
    public Response<?> delete(String key) {
        return run(() -> processor.newBinder("DELETE * FROM <table> WHERE(<p>)")
                .parameters(sqlTableModel.getPrimaryColumn().toParameter())
                .bindValue(key)
                .update()

        );
    }

    @Override
    public Response<T> usingId(String key) {
        return supply(() -> {
            QueryResult result = processor.newBinder("SELECT * FROM <table> WHERE(<p>)")
                    .parameters(sqlTableModel.getPrimaryColumn().toParameter())
                    .bindValue(key)
                    .query();

            return mapper.mapFields(result.getRow(key), modelClass);
        });
    }

    @Override
    public Response<Set<T>> all() {
        return supply(() -> {
            Set<T> all = new HashSet<>();

            QueryResult result = processor
                    .newBinder("SELECT * FROM <table>")
                    .query();

            for (Map<String, BasicTypeObject> basicObjectTypeMap : result) {
                all.add(mapper.mapFields(basicObjectTypeMap, modelClass));
            }

            return all;
        });
    }

    @Override
    public Response<Set<String>> keys() {
        return supply(() -> {
            QueryResult result = processor
                    .newBinder("SELECT <n> FROM <table>")
                    .bindString("<n>", sqlTableModel.getPrimaryColumn().getName())
                    .query();

            return new HashSet<>(result.getAllIdentifiers());
        });
    }

    public QueryProcessor getProcessor() {
        return processor;
    }
}
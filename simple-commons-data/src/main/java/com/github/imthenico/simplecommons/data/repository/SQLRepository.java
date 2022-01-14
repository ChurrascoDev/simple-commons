package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.db.sql.model.Constraint;
import com.github.imthenico.simplecommons.data.db.sql.model.SQLColumn;
import com.github.imthenico.simplecommons.data.db.sql.model.SQLTableModel;
import com.github.imthenico.simplecommons.data.db.sql.query.QueryBuilder;
import com.github.imthenico.simplecommons.data.db.sql.query.QueryResult;
import com.github.imthenico.simplecommons.data.key.SimpleSourceKey;
import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.data.repository.exception.UnknownTargetException;
import com.github.imthenico.simplecommons.data.repository.service.AbstractSQLSavingService;
import com.github.imthenico.simplecommons.data.repository.service.MySQLSavingService;
import com.github.imthenico.simplecommons.data.repository.service.SavingService;
import com.github.imthenico.simplecommons.util.Validate;
import com.github.imthenico.simplecommons.value.AbstractValue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executor;

import static com.github.imthenico.simplecommons.data.db.sql.query.QueryProcessor.executeQuery;
import static com.github.imthenico.simplecommons.data.db.sql.query.QueryProcessor.executeUpdate;

public class SQLRepository<T> extends AbstractRepository<T> {

    private final SQLTableModel sqlTableModel;
    private final SavingService<T> delegatedSaving;
    private final GenericMapper<?> mapper;
    private final Connection connection;

    public SQLRepository(
            Executor taskProcessor,
            Class<T> modelClass,
            GenericMapper<?> mapper,
            Connection connection,
            SQLTableModel sqlTableModel,
            AbstractSQLSavingService<T> delegatedSaving
    ) throws SQLException {
        super(taskProcessor, modelClass);
        this.sqlTableModel = Validate.notNull(sqlTableModel);
        this.delegatedSaving = Validate.notNull(delegatedSaving);
        this.mapper = Validate.notNull(mapper);
        this.connection = Validate.isTrue(connection != null && !connection.isClosed(), "Invalid Connection", connection);
        delegatedSaving.init(sqlTableModel, mapper, connection);
    }

    public SQLRepository(
            Executor taskProcessor,
            Class<T> modelClass,
            GenericMapper<?> genericMapper,
            Connection connection,
            SQLTableModel sqlTableModel
    ) throws SQLException {
       this(taskProcessor, modelClass, genericMapper, connection, sqlTableModel, new MySQLSavingService<>());
    }

    @Override
    public void save(T obj, SourceKey key) {
        delegatedSaving.save(obj, key);
    }

    @Override
    public int delete(SourceKey key) throws UnknownTargetException {
        return executeUpdate(
                QueryBuilder.create("DELETE FROM <t> WHERE(<p>)")
                        .table(sqlTableModel.getName())
                        .addPlaceholders(sqlTableModel.filterByConstraint(Constraint.PRIMARY).getName())
                        .prepare(connection)
                        .set(1, key)
                        .get(),
                true
        );
    }

    @Override
    public T usingId(SourceKey key) {
        String idColumn = sqlTableModel.filterByConstraint(Constraint.PRIMARY).getName();

        QueryResult result = executeQuery(
                QueryBuilder.create("SELECT * FROM <t> WHERE(<p>)")
                        .table(sqlTableModel.getName())
                        .addPlaceholder(idColumn)
                        .prepare(connection)
                        .set(1, key.getKey())
                        .get(),
                sqlTableModel,
                true
        );

        if (result == QueryResult.EMPTY)
            return null;

        return mapper.fromMap(result.getRow(idColumn), modelClass);
    }

    @Override
    public Set<T> all() {
         Set<T> all = new HashSet<>();

         QueryResult result = executeQuery(
                 QueryBuilder.create("SELECT * FROM <t>")
                         .table(sqlTableModel.getName())
                         .prepare(connection)
                         .get(),
                 sqlTableModel,
                 true
         );

        for (Map<String, AbstractValue> valueMap : result) {
            all.add(mapper.fromMap(valueMap, modelClass));
        }

        return all;
    }

    @Override
    public Set<SourceKey> keys() {
        Set<SourceKey> keys = new HashSet<>();
        SQLColumn column = sqlTableModel.filterByConstraint(Constraint.PRIMARY);

        QueryResult result = executeQuery(
                QueryBuilder.create("SELECT <n> FROM <t>")
                        .table(sqlTableModel.getName())
                        .replace("<n>", column.getName())
                        .prepare(connection)
                        .get(),
                sqlTableModel,
                true
        );

        result.iterator().forEachRemaining((map) -> keys.add(new SimpleSourceKey(map.get(column.getName()).getValue())));

        return keys;
    }
}
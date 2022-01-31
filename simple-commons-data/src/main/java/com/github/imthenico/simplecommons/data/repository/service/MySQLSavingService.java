package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.db.sql.query.QueryBuilder;
import com.github.imthenico.simplecommons.data.db.sql.query.StatementValuePacket;
import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.value.AbstractValue;

import java.util.Map;

import static com.github.imthenico.simplecommons.data.db.sql.query.QueryProcessor.executeUpdate;

public class MySQLSavingService<T> extends AbstractSQLSavingService<T> {

    @Override
    public void save(T obj, SourceKey key) {

        Map<String, AbstractValue> serializedObj = mapper.toMap(obj);
        StatementValuePacket valuePacket = StatementValuePacket.ofMap(serializedObj);

        executeUpdate(
                QueryBuilder.create("REPLACE INTO <t> VALUES(<p>)")
                        .table(sqlTableModel.getName())
                        .addParameters(sqlTableModel.getColumns().size())
                        .prepare(connection)
                        .set(1, key.getKey())
                        .open(valuePacket)
                        .get(),
                true
            );
    }
}
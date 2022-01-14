package com.github.imthenico.simplecommons.data.db.sql.query;

import com.github.imthenico.simplecommons.util.Validate;

public class QuerySchema {

    private final String tablePlaceholder;
    private final String parametersPlaceholder;

    public QuerySchema(
            String tablePlaceholder,
            String parametersPlaceholder
    ) {
        this.tablePlaceholder = Validate.notNull(tablePlaceholder);
        this.parametersPlaceholder = Validate.notNull(parametersPlaceholder);
    }

    public String getTablePlaceholder() {
        return tablePlaceholder;
    }

    public String getParametersPlaceholder() {
        return parametersPlaceholder;
    }
}
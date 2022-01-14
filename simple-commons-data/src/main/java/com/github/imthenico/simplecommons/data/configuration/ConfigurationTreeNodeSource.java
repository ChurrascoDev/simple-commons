package com.github.imthenico.simplecommons.data.configuration;

import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.util.Validate;

public class ConfigurationTreeNodeSource {

    private final String content;
    private final SourceKey key;

    public ConfigurationTreeNodeSource(
            String content,
            SourceKey key
    ) {
        this.content = Validate.notNull(content);
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public SourceKey getKey() {
        return key;
    }
}
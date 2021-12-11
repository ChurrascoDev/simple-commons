package com.github.imthenico.simplecommons.data.db.sql.connector;

import com.github.imthenico.simplecommons.data.db.Connector;
import com.github.imthenico.simplecommons.data.db.UserCredential;
import com.github.imthenico.simplecommons.util.Pair;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;

public class HikariConnector extends Connector<Connection> {

    private final Properties properties;
    private Consumer<HikariConfig> postConfigBuild = (config) -> {};
    private String jdbcUrl;

    public HikariConnector() {
        this.properties = new Properties();
    }

    public HikariConnector properties(Object... properties) {
        Pair<?, ?>[] pairs = Pair.of(true, properties);

        for (Pair<?, ?> pair : pairs) {
            this.properties.put(pair.getLeft(), pair.getRight());
        }

        return this;
    }

    public HikariConnector postConfigBuild(Consumer<HikariConfig> postBuildAction) {
        this.postConfigBuild = Objects.requireNonNull(postBuildAction);
        return this;
    }

    public HikariConnector jdbcUrl(String url) {
        this.jdbcUrl = Objects.requireNonNull(url);
        return this;
    }

    public Connection getHandle() {
        Objects.requireNonNull(jdbcUrl, "jdbcUrl");
        Objects.requireNonNull(credential, "credential");

        HikariConfig config = new HikariConfig(properties);
        config.setJdbcUrl(String.format(jdbcUrl, credential.getIp(), credential.getPort(), credential.getDatabase()));
        config.setUsername(credential.getUserName());
        config.setPassword(credential.getPassword());

        postConfigBuild.accept(config);

        try {
            return new HikariDataSource(config).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
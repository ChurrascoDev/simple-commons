package com.github.imthenico.simplecommons.data.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserCredential {

    private final String userName;
    private final String password;
    private final String ip;
    private final String port;
    private final String database;

    public UserCredential(String userName, String password, String ip, String port, String database) {
        this.userName = Objects.requireNonNull(userName);
        this.password = Objects.requireNonNull(password);
        this.ip = Objects.requireNonNull(ip);
        this.port = port != null && !port.equals("") ? port : "3306";
        this.database = Objects.requireNonNull(database);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password", password);
        map.put("ip", ip);
        map.put("port", port);
        map.put("database", database);

        return map;
    }

    public static UserCredential ofMap(Map<String, Object> objectMap) {
        return new UserCredential(
                (String) objectMap.get("userName"),
                (String) objectMap.get("password"),
                (String) objectMap.get("ip"),
                (String) objectMap.get("port"),
                (String) objectMap.get("database")
        );
    }
}
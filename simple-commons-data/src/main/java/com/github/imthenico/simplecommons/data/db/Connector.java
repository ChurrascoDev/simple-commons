package com.github.imthenico.simplecommons.data.db;

public abstract class Connector<T> {

    protected UserCredential credential;

    public Connector<T> credential(String userName, String pass, String ip, String port, String db) {
        this.credential = new UserCredential(userName, pass, ip, port, db);
        return this;
    }

    public abstract T getHandle();

}
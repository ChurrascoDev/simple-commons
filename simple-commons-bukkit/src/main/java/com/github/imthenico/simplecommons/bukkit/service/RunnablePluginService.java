package com.github.imthenico.simplecommons.bukkit.service;

public interface RunnablePluginService {

    void start();

    void end();

    default boolean isRunning() {
        return false;
    }

    default boolean async() {
        return false;
    }
}
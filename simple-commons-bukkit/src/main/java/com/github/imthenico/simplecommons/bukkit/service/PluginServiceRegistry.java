package com.github.imthenico.simplecommons.bukkit.service;

public interface PluginServiceRegistry extends Iterable<RunnablePluginService> {

    <T extends RunnablePluginService> T getService(String name);

    void loadService(String name, RunnablePluginService service) throws IllegalArgumentException;

}
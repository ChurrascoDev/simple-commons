package com.github.imthenico.simplecommons.bukkit.service;

import java.util.Map;

public interface PluginServiceRegistry {

    <T extends RunnablePluginService> T getService(Class<T> serviceClass);

    void loadService(AbstractPluginService service) throws IllegalArgumentException;

    Map<Class<?>, RunnablePluginService> getActiveServices();

}
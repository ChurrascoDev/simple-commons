package com.github.imthenico.simplecommons.bukkit.service;

import com.github.imthenico.simplecommons.bukkit.configuration.Configuration;
import com.github.imthenico.simplecommons.bukkit.event.ServiceEventType;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

public interface PluginServiceHelper {

    <T extends PluginService> void addListener(ServiceEventType eventType, Class<T> serviceClass, Consumer<T> action);

    Map<String, Object> getSharedData();

    <T> T getData(String key);

    Configuration pluginConfiguration();

    Configuration getConfiguration(String configFilePath);

    File getPluginFolder();

    boolean isRunning(Class<? extends PluginService> serviceClass);

    <T extends PluginService> T getService(Class<T> serviceClass);

}
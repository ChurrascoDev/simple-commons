package com.github.imthenico.simplecommons.bukkit.service;

import com.github.imthenico.simplecommons.bukkit.scheduler.OwnedScheduler;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePluginServiceRegistry implements PluginServiceRegistry {

    private final Map<Class<?>, RunnablePluginService> services;
    private final OwnedScheduler ownedScheduler;

    public SimplePluginServiceRegistry(Plugin plugin) {
        this.services = new ConcurrentHashMap<>();
        this.ownedScheduler = new OwnedScheduler(plugin);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RunnablePluginService> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    @Override
    public synchronized void loadService(AbstractPluginService service) throws IllegalArgumentException {
        Validate.isTrue(!services.containsKey(service.getClass()), "There's already another service with that name");
        Validate.isTrue(!service.isRunning(), "Service is already running");

        service.setPluginServiceRegistry(this);

        ownedScheduler.execute(() -> {
            service.start();
            services.put(service.getClass(), service);
        }, service.async(), 0);
    }

    @Override
    public Map<Class<?>, RunnablePluginService> getActiveServices() {
        return Collections.unmodifiableMap(services);
    }

    protected void handleServiceTermination(AbstractPluginService pluginService) {
        services.remove(pluginService.getClass());
    }
}
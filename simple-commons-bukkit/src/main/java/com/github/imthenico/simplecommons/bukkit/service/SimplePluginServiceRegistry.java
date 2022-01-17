package com.github.imthenico.simplecommons.bukkit.service;

import com.github.imthenico.simplecommons.bukkit.scheduler.OwnedScheduler;
import com.github.imthenico.simplecommons.iterator.UnmodifiableIterator;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimplePluginServiceRegistry implements PluginServiceRegistry {

    private final Map<String, RunnablePluginService> services;
    private final OwnedScheduler ownedScheduler;

    public SimplePluginServiceRegistry(Plugin plugin) {
        this.services = new ConcurrentHashMap<>();
        this.ownedScheduler = new OwnedScheduler(plugin);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RunnablePluginService> T getService(String name) {
        return (T) services.get(name);
    }

    @Override
    public synchronized void loadService(String name, RunnablePluginService service) throws IllegalArgumentException {
        Validate.isTrue(!services.containsKey(name), "There's already another service with that name");
        Validate.isTrue(!service.isRunning(), "Service is already running");

        DelegatedPluginService delegatedPluginService = new DelegatedPluginService(service);

        ownedScheduler.execute(() -> {
            delegatedPluginService.start();
            services.put(name, service);
        }, service.async(), 0);
    }

    @NotNull
    @Override
    public Iterator<RunnablePluginService> iterator() {
        return new UnmodifiableIterator<>(services.values().iterator());
    }

    public static class DelegatedPluginService implements RunnablePluginService {

        private final RunnablePluginService delegate;
        private boolean running;
        private boolean terminated;

        public DelegatedPluginService(RunnablePluginService delegate) {
            this.delegate = delegate;
        }

        @Override
        public void start() {
            if (terminated)
                return;

            if (isRunning())
                throw new UnsupportedOperationException("Service is already running");

            this.running = true;
            delegate.start();
        }

        @Override
        public void end() {
            if (!isRunning())
                throw new UnsupportedOperationException("Service is not running");

            this.running = false;
            this.terminated = true;
            delegate.end();
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        @Override
        public boolean async() {
            return delegate.async();
        }

        public RunnablePluginService getDelegate() {
            return delegate;
        }
    }
}
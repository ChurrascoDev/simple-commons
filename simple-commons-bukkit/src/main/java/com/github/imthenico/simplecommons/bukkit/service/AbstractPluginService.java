package com.github.imthenico.simplecommons.bukkit.service;

public abstract class AbstractPluginService implements RunnablePluginService {

    private boolean running;
    private boolean terminated;

    protected SimplePluginServiceRegistry pluginServiceRegistry;

    protected abstract void onEnd();

    protected abstract void onStart();

    @Override
    public final void start() {
        if (terminated)
            return;

        if (isRunning())
            throw new UnsupportedOperationException("Service is already running");

        this.running = true;
        onStart();
    }

    @Override
    public final void end() {
        if (!isRunning())
            throw new UnsupportedOperationException("Service is not running");

        this.running = false;
        this.terminated = true;
        pluginServiceRegistry.handleServiceTermination(this);
        onEnd();
    }

    @Override
    public final boolean isRunning() {
        return running;
    }

    void setPluginServiceRegistry(SimplePluginServiceRegistry pluginServiceRegistry) {
        if (this.pluginServiceRegistry != null)
            throw new UnsupportedOperationException("Owned service");

        this.pluginServiceRegistry = pluginServiceRegistry;
    }
}
package com.github.imthenico.simplecommons.bukkit.service;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public abstract class PluginService {

    private boolean loaded;
    private boolean running;
    protected JavaPlugin plugin;
    protected ConfigurationSection section;
    protected PluginServiceHelper helper;

    protected abstract boolean start();

    protected final boolean startService(ConfigurationSection section) {
        this.section = section;

        if (!loaded) {
            this.running = start();
            return running;
        }

        throw new UnsupportedOperationException("service is already loaded");
    }

    protected void end() {}

    protected final void endService() {
        if (running) {
            this.running = false;

            endService();
        }
    }

    protected void init(JavaPlugin plugin, PluginServiceHelper helper) {
        this.loaded = true;
        this.plugin = plugin;
        this.helper = helper;
    }

    public void reload() {}

    protected void configure(Config config) {}

    public final boolean isLoaded() {
        return loaded;
    }

    public final boolean isRunning() {
        return running;
    }

    public static final class Config {

        final PluginService owner;
        Consumer<ConfigurationSection> defaultValueApplier;
        Class<? extends PluginService> loadBeforeTarget;
        String sectionName;

        public Config(PluginService owner) {
            this.owner = owner;
        }

        public void configureSection(String name, Consumer<ConfigurationSection> sectionDefaults) {
            this.defaultValueApplier = sectionDefaults;
            this.sectionName = name;
        }

        public void configureSection(Consumer<ConfigurationSection> sectionDefaults) {
            this.configureSection(null, sectionDefaults);
        }

        public void setLoadBeforeTarget(Class<? extends PluginService> loadBeforeTarget) {
            this.loadBeforeTarget = loadBeforeTarget;
        }
    }
}
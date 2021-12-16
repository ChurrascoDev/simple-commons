package com.github.imthenico.simplecommons.bukkit.service;

import com.github.imthenico.simplecommons.bukkit.configuration.Configuration;
import com.github.imthenico.simplecommons.bukkit.event.*;
import com.github.imthenico.simplecommons.bukkit.listeners.PluginServiceManagerListener;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class PluginServiceManager implements Iterable<PluginService> {

    private final Map<Class<? extends PluginService>, PluginService> pluginServices;
    private final Map<ServiceEventType, Map<Class<? extends PluginService>, Set<Consumer<PluginService>>>> listeners;
    private final JavaPlugin plugin;
    private final PluginServiceHelper pluginServiceHelper;
    private boolean loaded;

    public PluginServiceManager(JavaPlugin plugin) {
        this.plugin = Validate.notNull(plugin);
        this.pluginServices = new LinkedHashMap<>();
        this.listeners = new LinkedHashMap<>();
        listeners.put(ServiceEventType.PRE_START, new HashMap<>());
        listeners.put(ServiceEventType.START, new HashMap<>());
        listeners.put(ServiceEventType.PRE_STOP, new HashMap<>());
        listeners.put(ServiceEventType.STOP, new HashMap<>());
        this.pluginServiceHelper = new PluginServiceHelperImpl(new Configuration(plugin, "config.yml"));
        Bukkit.getPluginManager().registerEvents(new PluginServiceManagerListener(listeners), plugin);
    }

    public PluginServiceManager registerService(PluginService pluginService) {
        Validate.notNull(pluginService, "pluginService");
        Validate.isTrue(!pluginServices.containsKey(pluginService.getClass()), "a service with the same class is already registered");

        this.pluginServices.put(pluginService.getClass(), pluginService);
        return this;
    }

    public PluginServiceManager loadAll() {
        if (loaded)
            throw new UnsupportedOperationException("service are already loaded");

        this.loaded = true;
        Map<Class<? extends PluginService>, Set<PluginService>> loadBefore = new HashMap<>();
        Map<PluginService, PluginService.Config> configMap = new HashMap<>();

        for (PluginService value : pluginServices.values()) {
            PluginService.Config config = new PluginService.Config(value);

            value.init(plugin, pluginServiceHelper);
            value.configure(config);

            handleConfiguration(loadBefore, value, config);
            configMap.put(value, config);
        }

        pluginServiceHelper.pluginConfiguration().save();

        for (PluginService value : pluginServices.values()) {
            startService(loadBefore, value, configMap);
        }

        return this;
    }

    public boolean isRunning(Class<? extends PluginService> serviceClass) {
        PluginService service = getService(serviceClass);

        return service != null && service.isLoaded();
    }

    @SuppressWarnings("unchecked")
    public <T extends PluginService> T getService(Class<? extends PluginService> serviceClass) {
        return (T) pluginServices.get(serviceClass);
    }

    public boolean stopService(Class<? extends PluginService> serviceClass) {
        PluginService service = getService(serviceClass);

        if (service != null) {
            if (isRunning(serviceClass)) {
                Bukkit.getPluginManager().callEvent(new PluginServicePreStopEvent(service));

                service.endService();

                Bukkit.getPluginManager().callEvent(new PluginServiceStopEvent(service));
                return true;
            }
        }

        return false;
    }

    private void startService(Map<Class<? extends PluginService>, Set<PluginService>> loadBefore, PluginService service,  Map<PluginService, PluginService.Config> configMap) {
        Set<PluginService> loadBeforeValue = loadBefore.get(service.getClass());

        if (loadBeforeValue != null && !loadBeforeValue.isEmpty()) {
            for (PluginService pluginService : loadBeforeValue) {
                startService(loadBefore, pluginService, configMap);
            }
        }

        Class<? extends PluginService> serviceClass = service.getClass();
        String name = serviceClass.getName();
        if (!isRunning(serviceClass)) {
            String sectionName = Validate.defIfNull(configMap.get(service).sectionName, name);

            try {
                Bukkit.getPluginManager().callEvent(new PluginServicePreStartEvent(service));

                boolean success = service.startService(pluginServiceHelper.pluginConfiguration().getConfigurationSection(sectionName));

                if (!success) {
                    Bukkit.getLogger().log(Level.WARNING, String.format("Unable to start service '%s'", name));
                } else {
                    Bukkit.getPluginManager().callEvent(new PluginServiceStartEvent(service));
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("An unexpected exception has occurred while starting service %s", name), e);
            }
        }
    }

    @Override
    public Iterator<PluginService> iterator() {
        return new Iterator<PluginService>() {
            private final Iterator<Class<? extends PluginService>> names = pluginServices.keySet().iterator();

            @Override
            public boolean hasNext() {
                return names.hasNext();
            }

            @Override
            public PluginService next() {
                return getService(names.next());
            }
        };
    }

    private void handleConfiguration(Map<Class<? extends PluginService>, Set<PluginService>> loadBefore, PluginService service, PluginService.Config config) {
        if (config.loadBeforeTarget != null) {
            Class<? extends PluginService> targetClass = config.loadBeforeTarget;
            PluginService target = pluginServices.get(targetClass);

            if (target != null) {
                Set<PluginService> loadBeforeTarget = loadBefore.computeIfAbsent(targetClass, k -> new LinkedHashSet<>());

                loadBeforeTarget.add(service);
            }
        }

        Consumer<ConfigurationSection> configurator = config.defaultValueApplier;
        Configuration configuration = pluginServiceHelper.pluginConfiguration();

        if (configurator != null) {
            String sectionName = Validate.defIfNull(config.sectionName, service.getClass().getName());

            ConfigurationSection section = configuration.getConfigurationSection(sectionName);

            if (section == null) {
                section = configuration.createSection(sectionName);

                configurator.accept(section);
            }
        }
    }

    private class PluginServiceHelperImpl implements PluginServiceHelper {
        private final Map<String, Object> sharedData = new HashMap<>();
        private final Map<String, Configuration> cachedConfiguration = new HashMap<>();
        private final Configuration pluginConfiguration;

        public PluginServiceHelperImpl(Configuration pluginConfiguration) {
            this.pluginConfiguration = pluginConfiguration;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends PluginService> void addListener(ServiceEventType eventType, Class<T> serviceClass, Consumer<T> action) {
            Set<Consumer<PluginService>> serviceListeners = listeners.get(Validate.notNull(eventType)).computeIfAbsent(serviceClass, k -> new LinkedHashSet<>());

            if (isRunning(serviceClass)) {
                action.accept(getService(serviceClass));
            } else {
                serviceListeners.add((Consumer<PluginService>) action);
            }
        }

        @Override
        public Map<String, Object> getSharedData() {
            return sharedData;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getData(String key) {
            return (T) sharedData.get(key);
        }

        @Override
        public Configuration pluginConfiguration() {
            return pluginConfiguration;
        }

        @Override
        public Configuration getConfiguration(String configFilePath) {
            Configuration configuration = cachedConfiguration.get(Validate.notNull(configFilePath));

            if (configuration == null) {
                File file = new File(plugin.getDataFolder(), configFilePath);

                if (file.exists() && file.isFile()) {
                    configuration = new Configuration(plugin, file);
                    cachedConfiguration.put(configFilePath, configuration);
                }
            }

            return configuration;
        }

        @Override
        public File getPluginFolder() {
            if (!plugin.getDataFolder().exists()) {
                Validate.isTrue(plugin.getDataFolder().mkdirs(), "unable to create plugin folder");
            }

            return plugin.getDataFolder();
        }

        @Override
        public boolean isRunning(Class<? extends PluginService> serviceClass) {
            return PluginServiceManager.this.isRunning(serviceClass);
        }

        @Override
        public <O extends PluginService> O getService(Class<O> serviceClass) {
            return PluginServiceManager.this.getService(serviceClass);
        }
    }
}
package com.github.imthenico.simplecommons.bukkit.listeners;

import com.github.imthenico.simplecommons.bukkit.event.PluginServicePreStartEvent;
import com.github.imthenico.simplecommons.bukkit.event.ServiceEventType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class PluginServiceManagerListener implements Listener {

    private final Map<ServiceEventType, Map<Class<? extends PluginService>, Set<Consumer<PluginService>>>> eventListeners;

    public PluginServiceManagerListener(Map<ServiceEventType, Map<Class<? extends PluginService>, Set<Consumer<PluginService>>>> eventListeners) {
        this.eventListeners = eventListeners;
    }

    @EventHandler
    public void onPreStart(PluginServicePreStartEvent event) {
        Set<Consumer<PluginService>> listeners = eventListeners.get(ServiceEventType.PRE_START).get(event.getPluginService().getClass());

        if (listeners != null) {
            listeners.forEach(action -> action.accept(event.getPluginService()));
        }
    }

    @EventHandler
    public void onStart(PluginServicePreStartEvent event) {
        Set<Consumer<PluginService>> listeners = eventListeners.get(ServiceEventType.START).get(event.getPluginService().getClass());

        if (listeners != null) {
            listeners.forEach(action -> action.accept(event.getPluginService()));
        }
    }

    @EventHandler
    public void onPreStop(PluginServicePreStartEvent event) {
        Set<Consumer<PluginService>> listeners = eventListeners.get(ServiceEventType.PRE_STOP).get(event.getPluginService().getClass());

        if (listeners != null) {
            listeners.forEach(action -> action.accept(event.getPluginService()));
        }
    }

    @EventHandler
    public void onStop(PluginServicePreStartEvent event) {
        Set<Consumer<PluginService>> listeners = eventListeners.get(ServiceEventType.STOP).get(event.getPluginService().getClass());

        if (listeners != null) {
            listeners.forEach(action -> action.accept(event.getPluginService()));
        }
    }
}
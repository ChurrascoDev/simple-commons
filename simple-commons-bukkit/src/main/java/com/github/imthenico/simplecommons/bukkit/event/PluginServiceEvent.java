package com.github.imthenico.simplecommons.bukkit.event;

import com.github.imthenico.simplecommons.bukkit.service.PluginService;
import org.bukkit.event.Event;

public abstract class PluginServiceEvent extends Event {

    private final PluginService pluginService;

    public PluginServiceEvent(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    public PluginService getPluginService() {
        return pluginService;
    }
}
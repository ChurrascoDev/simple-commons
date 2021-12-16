package com.github.imthenico.simplecommons.bukkit.event;

import com.github.imthenico.simplecommons.bukkit.service.PluginService;
import org.bukkit.event.HandlerList;

public class PluginServicePreStartEvent extends PluginServiceEvent {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    public PluginServicePreStartEvent(PluginService pluginService) {
        super(pluginService);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
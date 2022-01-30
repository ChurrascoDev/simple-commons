package com.github.imthenico.simplecommons.bukkit.util;

import com.github.imthenico.simplecommons.minecraft.LocationModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public interface Conversions {

    static LocationModel fromBukkitLocation(Location location) {
        return new LocationModel(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                location.getWorld().getName()
        );
    }

    static Location fromModel(LocationModel model) {
        return new Location(
                Bukkit.getWorld(model.getWorldName()),
                model.getX(),
                model.getY(),
                model.getZ(),
                model.getYaw(),
                model.getPitch()
        );
    }
}
package com.github.imthenico.simplecommons.minecraft;

import java.util.LinkedHashMap;
import java.util.Map;

public class LocationModel {

    protected double x, y, z;
    protected float yaw, pitch;
    private final String worldName;

    public LocationModel(double x, double y, double z, float yaw, float pitch, String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.worldName = worldName;
    }

    public LocationModel(double x, double y, double z, String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldName = worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getWorldName() {
        return worldName;
    }

    public LocationModel copy() {
        return new LocationModel(x, y, z, yaw, pitch, worldName);
    }

    public LocationModel add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public LocationModel add(double x, double y, double z, float yaw, float pitch) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.yaw += yaw;
        this.pitch += pitch;
        return this;
    }

    public LocationModel subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public LocationModel subtract(double x, double y, double z, float yaw, float pitch) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.yaw -= yaw;
        this.pitch -= pitch;
        return this;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> objectMap = new LinkedHashMap<>();

        objectMap.put("x", x);
        objectMap.put("y", y);
        objectMap.put("z", z);
        objectMap.put("yaw", yaw);
        objectMap.put("pitch", pitch);

        if (worldName != null)
            objectMap.put("worldName", worldName);

        return objectMap;
    }

    public static LocationModel deserialize(Map<String, Object> objectMap) {
        return new LocationModel(
                (double) objectMap.get("x"),
                (double) objectMap.get("y"),
                (double) objectMap.get("z"),
                (float) objectMap.get("yaw"),
                (float) objectMap.get("pitch"),
                (String) objectMap.get("worldName")
        );
    }
}
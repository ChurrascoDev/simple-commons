package com.github.imthenico.simplecommons.bukkit.scheduler;

import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class OwnedScheduler {

    private final Plugin owner;
    private final BukkitScheduler scheduler;

    public OwnedScheduler(Plugin owner) {
        this.owner = Validate.notNull(owner);
        this.scheduler = Bukkit.getScheduler();
    }

    public BukkitTask scheduleSync(Runnable action, long delay, long interval) {
        return scheduler.runTaskTimer(owner, action, delay, interval);
    }

    public BukkitTask scheduleAsync(Runnable action, long delay, long interval) {
        return scheduler.runTaskTimerAsynchronously(owner, action, delay, interval);
    }

    public int execute(Runnable action, boolean async, long delay) {
        if (async) {
            return scheduler.scheduleAsyncDelayedTask(owner, action, delay);
        }

        return scheduler.scheduleSyncDelayedTask(owner, action, delay);
    }

    public <V> CompletableFuture<V> callMethod(Callable<V> callable, boolean async) {
        CompletableFuture<V> completableFuture = new CompletableFuture<>();

        execute(() -> {
            try {
                completableFuture.complete(callable.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, async, 0);

        return completableFuture;
    }
}
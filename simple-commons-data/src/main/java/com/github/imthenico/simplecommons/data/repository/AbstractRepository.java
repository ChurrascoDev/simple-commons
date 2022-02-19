package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.repository.service.DeletionService;
import com.github.imthenico.simplecommons.data.repository.service.FindService;
import com.github.imthenico.simplecommons.data.repository.service.SavingService;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractRepository<T> implements SavingService<T>, DeletionService, FindService<T> {

    protected final Executor taskProcessor;
    protected final Class<T> modelClass;

    public AbstractRepository(
            Executor taskProcessor,
            Class<T> modelClass
    ) {
        this.taskProcessor = taskProcessor;
        this.modelClass = modelClass;
    }

    public boolean asyncMode() {
        return taskProcessor != null;
    }

    @Override
    public CompletableFuture<?> asyncSave(T obj, SourceKey key) {
        return run(() -> save(obj, key));
    }

    @Override
    public CompletableFuture<Integer> asyncDelete(SourceKey key) {
        return supply(() -> delete(key));
    }

    @Override
    public CompletableFuture<T> asyncFind(SourceKey key) {
        return supply(() -> usingId(key));
    }

    @Override
    public CompletableFuture<Set<T>> asyncAllCollection() {
        return supply(this::all);
    }

    @Override
    public CompletableFuture<Set<SourceKey>> asyncKeyCollection() {
        return supply(this::keys);
    }

    protected CompletableFuture<?> run(Runnable runnable) {
        return newFuture(() -> {
            runnable.run();
            return null;
        }, taskProcessor, Throwable::printStackTrace);
    }

    protected <O> CompletableFuture<O> supply(Supplier<O> supplier) {
        Validate.notNull(taskProcessor, "executor is null");

        return newFuture(supplier, taskProcessor, Throwable::printStackTrace);
    }

    private <O> CompletableFuture<O> newFuture(Supplier<O> resultSupplier, Executor executor, Consumer<Throwable> exceptionHandler) {
        CompletableFuture<O> future = executor != null ? CompletableFuture.supplyAsync(resultSupplier, executor) : CompletableFuture.completedFuture(resultSupplier.get());

        future.exceptionally(throwable -> {
            exceptionHandler.accept(throwable);
            return null;
        });

        return future;
    }

    private CompletableFuture<?> newFuture(Runnable runnable, Executor executor, Consumer<Throwable> exceptionHandler) {
        CompletableFuture<?> future = executor != null ? CompletableFuture.runAsync(runnable, executor) : CompletableFuture.runAsync(runnable);

        future.exceptionally(throwable -> {
            exceptionHandler.accept(throwable);
            return null;
        });

        return future;
    }
}
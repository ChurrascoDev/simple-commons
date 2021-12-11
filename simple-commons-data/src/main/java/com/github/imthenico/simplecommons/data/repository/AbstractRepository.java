package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.repository.service.DeletionService;
import com.github.imthenico.simplecommons.data.repository.service.FindService;
import com.github.imthenico.simplecommons.data.repository.service.SavingService;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.logging.Logger;

public abstract class AbstractRepository<T> implements SavingService<T>, DeletionService, FindService<T> {

    protected static final Logger REPOSITORY_LOGGER = Logger.getLogger("REPO_LOGGER");

    protected final Executor taskProcessor;
    protected final Class<T> modelClass;

    AbstractRepository(
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
    public Response<?> asyncSave(T obj, String key) {
        return run(() -> save(obj, key));
    }

    @Override
    public Response<?> asyncDelete(String key) {
        return run(() -> delete(key));
    }

    @Override
    public Response<T> asyncFind(String key) {
        return supply(() -> usingId(key));
    }

    @Override
    public Response<Set<T>> asyncAllCollection() {
        return supply(this::all);
    }

    @Override
    public Response<Set<String>> asyncKeyCollection() {
        return supply(this::keys);
    }

    protected Response<?> run(Runnable runnable) {
        return Response.newResponse(() -> {
            runnable.run();
            return null;
        }, taskProcessor, Throwable::printStackTrace);
    }

    protected <O> Response<O> supply(Supplier<O> supplier) {
        Validate.notNull(taskProcessor, "executor is null");

        return Response.newResponse(supplier, taskProcessor, Throwable::printStackTrace);
    }
}
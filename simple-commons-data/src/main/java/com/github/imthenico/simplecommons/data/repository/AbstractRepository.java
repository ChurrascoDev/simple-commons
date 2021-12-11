package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.repository.service.DeletionService;
import com.github.imthenico.simplecommons.data.repository.service.FindService;
import com.github.imthenico.simplecommons.data.repository.service.SavingService;

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

    protected Response<?> run(Runnable runnable) {
        return Response.newResponse(() -> {
            runnable.run();
            return null;
        }, taskProcessor, Throwable::printStackTrace);
    }

    protected <O> Response<O> supply(Supplier<O> supplier) {
        return Response.newResponse(supplier, taskProcessor, Throwable::printStackTrace);
    }
}
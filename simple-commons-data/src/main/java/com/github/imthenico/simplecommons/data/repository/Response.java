package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Response<T> {

    private final CompletableFuture<T> future;

    Response(CompletableFuture<T> future) {
        this.future = Validate.defIfNull(future, new CompletableFuture<>());
    }

    public CompletableFuture<T> future() {
        return future;
    }

    public boolean targetFound() {
        return future.isCompletedExceptionally();
    }

    public static <T> Response<T> newResponse(Supplier<T> resultSupplier, Executor executor, Consumer<Throwable> exceptionHandler) {
        CompletableFuture<T> future = executor != null ? CompletableFuture.supplyAsync(resultSupplier) : CompletableFuture.completedFuture(resultSupplier.get());

        future.exceptionally(throwable -> {
            exceptionHandler.accept(throwable);
            return null;
        });

        return new Response<>(future);
    }

    public static Response<?> newResponse(Runnable runnable, Executor executor, Consumer<Throwable> exceptionHandler) {
        CompletableFuture<?> future = executor != null ? CompletableFuture.runAsync(runnable, executor) : CompletableFuture.runAsync(runnable);

        future.exceptionally(throwable -> {
            exceptionHandler.accept(throwable);
            return null;
        });

        return new Response<>(future);
    }
}
package com.github.imthenico.simplecommons.data.repository.service;

import java.util.concurrent.CompletableFuture;

public interface SavingService<T> {

    CompletableFuture<?> asyncSave(T obj, String key);

    void save(T obj, String key);

}
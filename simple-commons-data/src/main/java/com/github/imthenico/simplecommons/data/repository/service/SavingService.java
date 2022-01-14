package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.key.SourceKey;

import java.util.concurrent.CompletableFuture;

public interface SavingService<T> {

    CompletableFuture<?> asyncSave(T obj, SourceKey key);

    void save(T obj, SourceKey key);

}
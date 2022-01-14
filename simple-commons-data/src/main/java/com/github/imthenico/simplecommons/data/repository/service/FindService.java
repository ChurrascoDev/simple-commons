package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.key.SourceKey;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface FindService<T> {

    CompletableFuture<T> asyncFind(SourceKey key);

    CompletableFuture<Set<T>> asyncAllCollection();

    CompletableFuture<Set<SourceKey>> asyncKeyCollection();

    T usingId(SourceKey key);

    Set<T> all();

    Set<SourceKey> keys();

}
package com.github.imthenico.simplecommons.data.repository.service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface FindService<T> {

    CompletableFuture<T> asyncFind(String key);

    CompletableFuture<Set<T>> asyncAllCollection();

    CompletableFuture<Set<String>> asyncKeyCollection();

    T usingId(String key);

    Set<T> all();

    Set<String> keys();

}
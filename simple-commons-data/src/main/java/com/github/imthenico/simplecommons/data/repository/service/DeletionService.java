package com.github.imthenico.simplecommons.data.repository.service;

import java.util.concurrent.CompletableFuture;

public interface DeletionService {

    CompletableFuture<?> asyncDelete(String key);

    void delete(String key);

}
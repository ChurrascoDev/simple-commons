package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.repository.exception.UnknownTargetException;

import java.util.concurrent.CompletableFuture;

public interface DeletionService {

    CompletableFuture<?> asyncDelete(String key);

    void delete(String key) throws UnknownTargetException;

}
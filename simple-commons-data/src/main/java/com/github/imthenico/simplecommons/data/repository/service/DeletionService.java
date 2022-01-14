package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.repository.exception.UnknownTargetException;

import java.util.concurrent.CompletableFuture;

public interface DeletionService {

    CompletableFuture<Integer> asyncDelete(SourceKey key);

    int delete(SourceKey key) throws UnknownTargetException;

}
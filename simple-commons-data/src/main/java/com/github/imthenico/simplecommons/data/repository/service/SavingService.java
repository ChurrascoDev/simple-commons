package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.repository.Response;

public interface SavingService<T> {

    Response<?> asyncSave(T obj, String key);

    void save(T obj, String key);

}
package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.repository.Response;

public interface SavingService<T> {

    Response<?> save(T obj, String key);

}
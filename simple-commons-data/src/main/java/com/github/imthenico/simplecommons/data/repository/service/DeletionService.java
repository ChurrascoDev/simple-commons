package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.repository.Response;

public interface DeletionService {

    Response<?> asyncDelete(String key);

    void delete(String key);

}
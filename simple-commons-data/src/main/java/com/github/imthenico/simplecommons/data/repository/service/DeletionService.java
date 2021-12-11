package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.repository.Response;

public interface DeletionService {

    Response<?> delete(String key);

}
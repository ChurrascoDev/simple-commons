package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.repository.Response;

import java.util.Set;

public interface FindService<T> {

    Response<T> usingId(String key);

    Response<Set<T>> all();

    Response<Set<String>> keys();

}
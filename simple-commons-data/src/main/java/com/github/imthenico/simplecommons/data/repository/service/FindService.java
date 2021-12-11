package com.github.imthenico.simplecommons.data.repository.service;

import com.github.imthenico.simplecommons.data.repository.Response;

import java.util.Set;

public interface FindService<T> {

    Response<T> asyncFind(String key);

    Response<Set<T>> asyncAllCollection();

    Response<Set<String>> asyncKeyCollection();

    T usingId(String key);

    Set<T> all();

    Set<String> keys();

}
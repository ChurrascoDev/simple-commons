package com.github.imthenico.simplecommons.data.repository;

import com.github.imthenico.simplecommons.data.repository.exception.SerializationException;
import com.github.imthenico.simplecommons.object.BasicTypeObject;

import java.util.Map;

public interface GenericMapper<T> {

    <O> O map(T readableData, Class<O> targetClass);

    T serialize(Object obj) throws SerializationException;

    <O> O mapFields(Map<String, BasicTypeObject> data, Class<O> targetClass);

    Map<String, Object> serializeFields(Object toSerialize) throws SerializationException;

}
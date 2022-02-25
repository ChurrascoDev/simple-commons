package com.github.imthenico.simplecommons.data.mapper;

import com.github.imthenico.simplecommons.value.AbstractValue;

import java.util.Map;

public interface GenericMapper<T> {

    <O> O map(T readableData, Class<O> targetClass);

    T serialize(Object obj);

    <O> O fromMap(Map<String, ?> data, Class<O> targetClass);

    Map<String, AbstractValue> toMap(Object toSerialize);

    <O> O mapDirectly(Object toSerialize, Class<O> target);
}
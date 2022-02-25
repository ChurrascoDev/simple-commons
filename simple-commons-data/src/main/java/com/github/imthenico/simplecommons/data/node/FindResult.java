package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.data.util.ThrowableSupplier;
import com.github.imthenico.simplecommons.util.list.CustomList;

import java.util.function.Predicate;

public interface FindResult extends Iterable<NodeValue> {

    boolean hasTargetsFound();

    boolean isHomogeneous(Class<?> clazz);

    NodeValue getValue(int index);

    Object get(int index);

    <T> T castOrThrow(int index, ThrowableSupplier throwable);

    <T> T getOrThrow(int index, ThrowableSupplier throwable);

    <T> T getOrDefault(int index, T def);

    <T> T getElement(int index);

    Object first();

    <T> T firstElement();

    Object last();

    <T> T lastElement();

    int resultCount();

    <T> CustomList<T> toList(boolean ignoreNulls);

    <T> CustomList<T> toList(Predicate<Object> filter, boolean ignoreNulls);

    <T> CustomList<T> toList(Predicate<Object> filter);

    <T> CustomList<T> toList();

    CustomList<NodeValue> internalCopy();

}
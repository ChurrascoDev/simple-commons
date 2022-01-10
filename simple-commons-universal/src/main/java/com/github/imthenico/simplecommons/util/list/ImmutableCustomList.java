package com.github.imthenico.simplecommons.util.list;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.AbstractList;
import java.util.Optional;

public final class ImmutableCustomList<E> extends AbstractList<E> implements CustomList<E> {

    private final CustomList<E> original;

    ImmutableCustomList(CustomList<E> original) {
        this.original = Validate.notNull(original);
    }

    @Override
    public java.util.List<E> commonList() {
        return original.commonList();
    }

    @Override
    public Optional<E> safeGet(int index) {
        return original.safeGet(index);
    }

    @Override
    public boolean mutable() {
        return false;
    }

    @Override
    public ImmutableCustomList<E> immutableCopy() {
        return new ImmutableCustomList<>(this);
    }

    @Override
    public CustomList<E> copy() {
        return original.copy();
    }

    @Override
    public E get(int index) {
        return original.get(index);
    }

    @Override
    public int size() {
        return original.size();
    }

    @Override
    public Optional<E> random() {
        return original.random();
    }
}
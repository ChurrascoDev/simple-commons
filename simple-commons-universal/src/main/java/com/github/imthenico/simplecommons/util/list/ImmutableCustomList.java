package com.github.imthenico.simplecommons.util.list;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.AbstractList;
import java.util.Optional;

public class ImmutableCustomList<E> extends AbstractList<E> implements CustomList<E> {

    private final SimpleCustomList<E> original;

    protected ImmutableCustomList(SimpleCustomList<E> original) {
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
        return new ImmutableCustomList<>(original);
    }

    @Override
    public CustomList<E> copy() {
        return CustomList.of(original.listProvider, original.list);
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
package com.github.imthenico.simplecommons.util.list;

import java.util.*;
import java.util.function.Function;

public final class SimpleCustomList<E> extends AbstractList<E> implements CustomList<E> {

    private static final Random RANDOM = new Random();

    private final Function<List<E>, List<E>> listProvider;
    private final List<E> list;

    SimpleCustomList(
            Function<List<E>, List<E>> listProvider,
            List<E> list
    ) {
        this.listProvider = listProvider;
        this.list = list;
    }

    @Override
    public List<E> commonList() {
        return listProvider.apply(list);
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public Optional<E> safeGet(int index) {
        if (index >= list.size())
            return Optional.empty();

        return Optional.ofNullable(get(index));
    }

    @Override
    public E set(int index, E element) {
        return list.set(index, element);
    }

    @Override
    public boolean add(E element) {
        return list.add(element);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean mutable() {
        return true;
    }

    @Override
    public ImmutableCustomList<E> immutableCopy() {
        return new ImmutableCustomList<>(this);
    }

    @Override
    public CustomList<E> copy() {
        return new SimpleCustomList<>(listProvider, listProvider.apply(list));
    }

    @Override
    public Optional<E> random() {
        int index = RANDOM.nextInt(size() - 1);
        return safeGet(index);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }
}
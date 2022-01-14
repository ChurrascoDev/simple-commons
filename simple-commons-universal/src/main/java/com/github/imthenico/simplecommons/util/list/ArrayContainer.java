package com.github.imthenico.simplecommons.util.list;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayContainer<E> implements Iterable<E> {

    private final E[] original;

    public ArrayContainer(E[] original) {
        this.original = Validate.notNull(original);
    }

    public SimpleCustomList<E> getAsList() {
        return CustomList.of(Arrays.asList(original));
    }

    public E[] getCopy() {
        return Arrays.copyOf(original, original.length);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int position = -1;

            @Override
            public boolean hasNext() {
                return position < original.length;
            }

            @Override
            public E next() {
                position++;
                return original[position];
            }
        };
    }

    public static <E> ArrayContainer<E> copy(E[] value) {
        return new ArrayContainer<>(Arrays.copyOf(value, value.length));
    }
}
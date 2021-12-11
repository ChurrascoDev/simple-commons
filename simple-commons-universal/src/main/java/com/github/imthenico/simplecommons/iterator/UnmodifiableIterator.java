package com.github.imthenico.simplecommons.iterator;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.Iterator;

public class UnmodifiableIterator<T> implements Iterator<T> {

    private final Iterator<T> delegate;

    public UnmodifiableIterator(Iterator<T> delegate) {
        this.delegate = Validate.notNull(delegate);
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public T next() {
        return delegate.next();
    }
}
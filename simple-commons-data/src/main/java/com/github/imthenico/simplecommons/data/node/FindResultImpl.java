package com.github.imthenico.simplecommons.data.node;

import com.github.imthenico.simplecommons.data.node.value.SimpleNodeValue;
import com.github.imthenico.simplecommons.data.util.ThrowableSupplier;
import com.github.imthenico.simplecommons.iterator.UnmodifiableIterator;
import com.github.imthenico.simplecommons.util.list.CustomList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class FindResultImpl implements FindResult {

    private final List<NodeValue> foundElements;

    FindResultImpl(List<NodeValue> foundElements) {
        this.foundElements = foundElements;
    }

    @Override
    public boolean hasTargetsFound() {
        return !foundElements.isEmpty();
    }

    @Override
    public boolean isHomogeneous(Class<?> clazz) {
        if (!hasTargetsFound())
            return false;

        for (NodeValue foundElement : foundElements) {
            Object value = foundElement.getValue();

            if (value != null && !clazz.isAssignableFrom(value.getClass())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public NodeValue getValue(int index) {
        if (index >= foundElements.size())
            return SimpleNodeValue.EMPTY;

        return foundElements.get(index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T castOrThrow(int index, ThrowableSupplier throwable) {
        try {
            return (T) get(index);
        } catch (Exception e) {
            try {
                throw throwable.get();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOrThrow(int index, ThrowableSupplier throwable) {
        Object obj = get(index);

        try {
            if (obj == null)
                throw throwable.get();

            return (T) obj;
        } catch (ClassCastException e) {
            try {
                throw throwable.get();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }  catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object get(int index) {
        if (index >= foundElements.size())
            return null;

        NodeValue nodeValue = foundElements.get(index);

        if (nodeValue == null || nodeValue.isNull())
            return null;

        if (nodeValue.getAsSimpleValue().isPresent())
            return nodeValue.getAsSimpleValue().get().getValue();

        if (nodeValue.getAsNode().isPresent())
            return nodeValue.getValue();

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(int index, T def) {
        if (index >= foundElements.size())
            return null;

        Object found = get(index);

        if (found == null)
            return def;

        try {
            return (T) found;
        } catch (Exception e) {
            return def;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getElement(int index) {
        if (index >= foundElements.size())
            return null;

        return (T) foundElements.get(index).getValue();
    }

    @Override
    public Object first() {
        return get(0);
    }

    @Override
    public <T> T firstElement() {
        return getElement(0);
    }

    @Override
    public Object last() {
        return get(foundElements.size() - 1);
    }

    @Override
    public <T> T lastElement() {
        return getElement(foundElements.size() - 1);
    }

    @Override
    public int resultCount() {
        return foundElements.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> CustomList<T> toList(Predicate<Object> filter, boolean ignoreNulls) {
        CustomList<T> customList = CustomList.create(LinkedList::new);

        for (NodeValue foundElement : foundElements) {
            Object value = foundElement.getValue();

            if (value == null)
                continue;

            if (filter.test(value)) {
                customList.add((T) value);
            }
        }

        return customList;
    }

    @Override
    public <T> CustomList<T> toList(boolean ignoreNulls) {
        return toList((obj) -> true, ignoreNulls);
    }

    @Override
    public <T> CustomList<T> toList(Predicate<Object> filter) {
        return toList(filter, true);
    }

    @Override
    public <T> CustomList<T> toList() {
        return toList(true);
    }

    @Override
    public CustomList<NodeValue> internalCopy() {
        return CustomList.of(LinkedList::new, foundElements);
    }

    @NotNull
    @Override
    public Iterator<NodeValue> iterator() {
        return new UnmodifiableIterator<>(foundElements.iterator());
    }
}
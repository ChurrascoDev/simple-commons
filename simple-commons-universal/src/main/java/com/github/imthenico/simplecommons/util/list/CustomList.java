package com.github.imthenico.simplecommons.util.list;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface CustomList<E> extends List<E> {

    List<E> commonList();

    Optional<E> safeGet(int index);

    boolean mutable();

    ImmutableCustomList<E> immutableCopy();

    CustomList<E> copy();

    default boolean isOutOfBounds(int index) {
        return index >= size();
    }

    Optional<E> random();

    static <E> SimpleCustomList<E> create(Function<List<E>, List<E>> listProvider) {
        return new SimpleCustomList<>(
                Validate.notNull(listProvider),
                listProvider.apply(new ArrayList<>())
        );
    }

    static <E> SimpleCustomList<E> create() {
        return create(ArrayList::new);
    }

    static <E> SimpleCustomList<E> of(
            Function<List<E>, List<E>> listProvider,
            List<E> list
    ) {
        // check if list is mutable
        list.addAll(Collections.emptyList());

        return new SimpleCustomList<>(
                listProvider,
                list
        );
    }

    static <E> SimpleCustomList<E> of(List<E> list) {
        return of(ArrayList::new, list);
    }
}
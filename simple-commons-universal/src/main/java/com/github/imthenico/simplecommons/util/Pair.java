package com.github.imthenico.simplecommons.util;

public class Pair<L, R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @SuppressWarnings("unchecked")
    public static <L, R> Pair<L, R>[] of(boolean notNullArgs, Object... objects) {
        if (objects.length % 2 != 0)
            throw new IllegalArgumentException("cannot separate in pairs, the array length is not divisible by 2");

        Pair<L, R>[] pairs = new Pair[objects.length / 2];

        int i = 0;
        for (int j = 0; j < objects.length; j++) {
            Object left = objects[j];
            j++;
            Object right = objects[j];

            if ((left == null || right == null) && notNullArgs)
                throw new NullPointerException();

            pairs[i] = new Pair<>((L) left, (R) right);

            i++;
        }

        return pairs;
    }

    public static <L, R> Pair<L, R>[] of(Object... objects) {
        return of(false, objects);
    }
}
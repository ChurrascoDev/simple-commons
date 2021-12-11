package com.github.imthenico.simplecommons.reflection;

import com.github.imthenico.simplecommons.util.Validate;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReflectionUtil {

    private final boolean cacheQueries;
    private final Map<Object, Accessible<?>> cache;

    public ReflectionUtil(boolean cacheQueries) {
        this.cacheQueries = cacheQueries;
        this.cache = new HashMap<>();
    }

    public ReflectionUtil() {
        this(false);
    }

    public Field getField(Class<?> clazz, String name) {
        try {
            java.lang.reflect.Field field = clazz.getDeclaredField(name);

            return getOrCache(new AccessibleIdentifier(clazz, name, new Class[0]), new Field(field));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Method getMethod(Class<?> clazz, String name, Class<?>[] args) {
        try {
            java.lang.reflect.Method method = clazz.getDeclaredMethod(name, args);

            return getOrCache(new AccessibleIdentifier(clazz, name, args), new Method(method));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>[] args) {
        try {
            java.lang.reflect.Constructor<T> constructor = clazz.getDeclaredConstructor(args);

            return getOrCache(new AccessibleIdentifier(clazz, null, args), new Constructor<>(constructor));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ReflectionUtil assignConstructorId(String id, Class<?> clazz, Class<?>[] args) {
        try {
            java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor(args);

            Constructor<?> constructorImpl = new Constructor<>(constructor);
            if (!cache.containsKey(id)) {
                cache.put(id, constructorImpl);
            }

            return this;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return this;
    }

    public <T> T newInstance(Class<?> clazz, Class<?>[] argsTypes, Object[] args) {
        return getConstructor(clazz, argsTypes).get(null, args);
    }

    public <T> T createUsingId(String id, Object... args) {
        Accessible<?> found = cache.get(id);
        if (found instanceof Constructor) {
            return found.get(null, args);
        }

        return null;
    }

    public <T> T createWithNotNullArgs(Class<?> clazz, Object... args) {
        return newInstance(clazz, getClasses(args), args);
    }

    protected Class<?>[] getClasses(Object... objects) {
        Class<?>[] classes = new Class[objects.length];

        for (int i = 0; i < classes.length; i++) {
            classes[i] = Validate.notNull(objects[i], "null arg").getClass();
        }

        return classes;
    }

    @SuppressWarnings("unchecked")
    private <T extends Accessible<?>> T getOrCache(AccessibleIdentifier identifier, T def) {
        if (cacheQueries) {
            if (cache.containsKey(identifier)) {
                return (T) cache.get(identifier);
            } else {
                cache.put(identifier, def);
            }
        }

        return def;
    }

    public static class Constructor<T> extends Accessible<java.lang.reflect.Constructor<T>> {
        private Constructor(java.lang.reflect.Constructor<T> handle) {
            super(handle);
        }

        @Override
        protected Object getValue(Object obj, Object... args) throws IllegalAccessException {
            try {
                return handle.newInstance(args);
            } catch (InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class Method extends Accessible<java.lang.reflect.Method> {

        private Method(java.lang.reflect.Method handle) {
            super(handle);
        }

        @Override
        protected Object getValue(Object obj, Object... args) throws IllegalAccessException {
            try {
                return handle.invoke(obj, args);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class Field extends Accessible<java.lang.reflect.Field> {

        private Field(java.lang.reflect.Field handle) {
            super(handle);
        }

        @Override
        protected Object getValue(Object obj, Object... args) throws IllegalAccessException {
            return handle.get(obj);
        }
    }

    private static class AccessibleIdentifier {
        private final Class<?> clazz;
        private final String name;
        private final Class<?>[] argTypes;

        public AccessibleIdentifier(Class<?> clazz, String name, Class<?>[] argTypes) {
            this.clazz = clazz;
            this.name = name;
            this.argTypes = argTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccessibleIdentifier that = (AccessibleIdentifier) o;
            return clazz.equals(that.clazz) && Objects.equals(name, that.name) && Arrays.equals(argTypes, that.argTypes);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(clazz, name);
            result = 31 * result + Arrays.hashCode(argTypes);
            return result;
        }
    }
}
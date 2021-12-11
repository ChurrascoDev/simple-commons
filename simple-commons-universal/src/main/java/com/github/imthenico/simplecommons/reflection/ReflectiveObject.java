package com.github.imthenico.simplecommons.reflection;

import com.github.imthenico.simplecommons.util.Validate;

public class ReflectiveObject<T> {

    private final T obj;
    private final ReflectionUtil reflectionUtil;

    public ReflectiveObject(T obj) {
        this.obj = Validate.notNull(obj);

        Validate.isTrue(!obj.getClass().isArray(), "invalid object type (is array), the object must be single");
        this.reflectionUtil = new ReflectionUtil(true);
    }

    public <O> O getField(String name) {
        return reflectionUtil.getField(obj.getClass(), name).get(obj);
    }

    public ReflectionUtil.Method invoke(String name, Class<?>[] argsTypes, Object[] args) {
        return reflectionUtil.getMethod(obj.getClass(), name, argsTypes).get(obj, args);
    }

    public ReflectionUtil.Method invokeWithNotNullArgs(String name, Object[] args) {
        return invoke(name, reflectionUtil.getClasses(args), args);
    }

    public T getObject() {
        return obj;
    }
}
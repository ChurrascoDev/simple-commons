package com.github.imthenico.simplecommons.data.key;

public interface SourceKey {

    <T> T getKey();

    <E> E getExtraData(String dataKey, E def);

}
package com.github.imthenico.simplecommons.data.mapper;

import com.github.imthenico.simplecommons.data.exception.MappingException;

public interface Mappable {

    <T> T map(Class<T> target) throws MappingException;

}
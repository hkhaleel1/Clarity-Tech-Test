package com.clarity.model.converter;

public interface Converter<S, T>
{
    T toDTO(S s);
    S toEntity(T t);
}
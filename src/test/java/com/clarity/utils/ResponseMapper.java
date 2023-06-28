package com.clarity.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class ResponseMapper<T>
{
    private final ObjectMapper mapper = new ObjectMapper();

    public T deserialize(final String response, final Class<T> valueType) throws JsonProcessingException
    {
        return mapper.readValue(response, valueType);
    }

    public List<T> deserializeList(final String response, final Class<T> valueType) throws JsonProcessingException
    {
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, valueType);
        return mapper.readValue(response, listType);
    }

    public String asJsonString(final Object obj)
    {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
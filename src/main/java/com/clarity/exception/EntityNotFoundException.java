package com.clarity.exception;

public class EntityNotFoundException extends Exception
{
    public EntityNotFoundException(String message) {
        super(message);
    }
}
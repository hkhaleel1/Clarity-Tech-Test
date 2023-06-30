package com.clarity.auth.repository;

public interface ApiKeyRepository
{
    boolean isValidKey(String authToken);
}
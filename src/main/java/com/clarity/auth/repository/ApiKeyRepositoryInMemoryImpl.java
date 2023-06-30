package com.clarity.auth.repository;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class ApiKeyRepositoryInMemoryImpl implements ApiKeyRepository
{
    private static final String AUTH_TOKEN = "clarity";
    private final List<String> apiKeyMap = Collections.singletonList(AUTH_TOKEN);

    @Override
    public boolean isValidKey(String authToken)
    {
        final Optional<String> opKey
                = apiKeyMap.stream().filter(k -> k.equals(authToken)).findFirst();
        return opKey.isPresent();
    }
}
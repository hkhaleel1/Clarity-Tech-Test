package com.clarity.auth.service;

import com.clarity.auth.model.ApiKeyAuthentication;
import com.clarity.auth.repository.ApiKeyRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService
{
    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public Authentication getAuthentication(HttpServletRequest request)
    {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKeyRepository.isValidKey(apiKey)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
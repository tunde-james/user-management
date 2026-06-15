package com.example.usermanagement.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import com.example.usermanagement.config.JwtProperties;

@Service
public class TokenBlocklistService {

    private final Cache<String, Boolean> revokedTokens;

    public TokenBlocklistService(JwtProperties jwtProperties) {
        this.revokedTokens = Caffeine.newBuilder()
                .expireAfterWrite(jwtProperties.getExpiresIn())
                .maximumSize(100_000)
                .build();
    }

    public void revoke(String token) {
        revokedTokens.put(token, Boolean.TRUE);
    }

    public boolean isRevoked(String token) {
        return revokedTokens.getIfPresent(token) != null;
    }
}

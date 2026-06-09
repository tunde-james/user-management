package com.example.usermanagement.dtos.auth;

public record RegisterResDto(
        Long id,
        String username,
        String email,
        String role,
        String accessToken,
        String tokenType,
        Long expiresInSeconds) {}

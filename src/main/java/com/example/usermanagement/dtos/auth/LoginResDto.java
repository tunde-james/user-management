package com.example.usermanagement.dtos.auth;

public record LoginResDto(String accessToken, String tokenType, Long expiresInSeconds) {}


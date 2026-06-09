package com.example.usermanagement.dtos.user;

import com.example.usermanagement.enums.UserRole;

public record UserResDto(Long id, String username, String email, boolean active, UserRole role) {}

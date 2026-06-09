package com.example.usermanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import com.example.usermanagement.enums.UserRole;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email is required")
    @Email(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Please provide a valid email address")
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
}

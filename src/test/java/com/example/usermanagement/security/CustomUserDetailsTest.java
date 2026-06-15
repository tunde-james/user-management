package com.example.usermanagement.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.usermanagement.entity.User;
import com.example.usermanagement.enums.UserRole;

class CustomUserDetailsTest {

    @Test
    void isEnabled_WhenUserIsActive_ShouldReturnTrue() {

        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("hashed_password");
        user.setActive(true);
        user.setRole(UserRole.ROLE_USER);

        CustomUserDetails underTest = new CustomUserDetails(user);

        boolean actual = underTest.isEnabled();

        assertTrue(actual, "Expected isEnabled() to return true when user is active");
    }

    @Test
    void isEnabled_WhenUserIsNotActive_ShouldReturnFalse() {

        User user = new User();
        user.setUsername("inactive_user");
        user.setEmail("inactive@example.com");
        user.setPassword("hashed_password");
        user.setActive(false);
        user.setRole(UserRole.ROLE_USER);

        CustomUserDetails underTest = new CustomUserDetails(user);

        boolean actual = underTest.isEnabled();

        assertFalse(actual, "Expected isEnabled to return false when user is not active");
    }

    @Test
    void isAccountNonExpired_WhenUserIsNotExpired_ShouldReturnTrue() {

        User user = new User();
        user.setExpired(false);

        CustomUserDetails underTest = new CustomUserDetails(user);

        boolean actual = underTest.isAccountNonExpired();

        assertTrue(actual, "Expected isAccountNonExpired to return true when user is not expired");
    }

    @Test
    void isAccountNonExpired_WhenUserIsExpired_ShouldReturnFalse() {

        User user = new User();
        user.setExpired(true);

        CustomUserDetails underTest = new CustomUserDetails(user);

        boolean actual = underTest.isAccountNonExpired();

        assertFalse(actual, "Expected isAccountNonExpired to return false when user is expired");
    }

    @Test
    void isAccountNonLocked_WhenUserIsNotLocked_ShouldReturnTrue() {
        
        User user = new User();
        user.setUsername("active_user");
        user.setEmail("active@example.com");
        user.setPassword("hashed_password");
        user.setLocked(false);
        user.setRole(UserRole.ROLE_USER);

        CustomUserDetails underTest = new CustomUserDetails(user);

        boolean actual = underTest.isAccountNonLocked();

        assertTrue(actual, "Expected isAccountNonLocked() to return true when user is not locked");
    }

    @Test
    void isCredentialsNonExpired_WhenUserCredentialsAreNotExpired_ShouldReturnTrue() {
        
        User user = new User();
        user.setUsername("fresh_password_user");
        user.setEmail("fresh@example.com");
        user.setPassword("hashed_password");
        user.setCredentialsExpired(false);
        user.setRole(UserRole.ROLE_USER);

        CustomUserDetails underTest = new CustomUserDetails(user);

        boolean actual = underTest.isCredentialsNonExpired();

        assertTrue(actual, "Expected isCredentialsNonExpired() to return true when user credentials are not expired");
    }
}

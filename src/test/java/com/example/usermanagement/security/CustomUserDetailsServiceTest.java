package com.example.usermanagement.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.usermanagement.entity.User;
import com.example.usermanagement.enums.UserRole;
import com.example.usermanagement.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService underTest;

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {

        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("hashed_password");
        user.setActive(true);
        user.setRole(UserRole.ROLE_USER);

        when(userRepository.findByUsernameIgnoreCase("john")).thenReturn(Optional.of(user));

        UserDetails result = underTest.loadUserByUsername("john");

        assertThat(result).isInstanceOf(CustomUserDetails.class);
        assertThat(result.getUsername()).isEqualTo("john");
    }

    @Test
    void loadUserByUsername_WhenUserNotFoundByUsername_ShouldTryEmail() {

        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("hashed_password");
        user.setActive(true);
        user.setRole(UserRole.ROLE_USER);

        when(userRepository.findByUsernameIgnoreCase("john@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByEmailIgnoreCase("john@example.com")).thenReturn(Optional.of(user));

        UserDetails result = underTest.loadUserByUsername("john@example.com");

        assertThat(result.getUsername()).isEqualTo("john");
    }
}

package com.example.usermanagement.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.usermanagement.entity.User;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {

        return !user.isExpired();
    }

    @Override
    public boolean isAccountNonLocked() {

        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return !user.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {

        return user.isActive();
    }

    public User getUser() {

        return user;
    }
}

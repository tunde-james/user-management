package com.example.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {}

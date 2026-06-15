package com.example.usermanagement.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagement.dtos.user.ChangePasswordReqDto;
import com.example.usermanagement.dtos.user.UserResDto;
import com.example.usermanagement.dtos.user.UserUpdateReqDto;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.InvalidPasswordException;
import com.example.usermanagement.exception.UserAlreadyExistsException;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResDto getUserById(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        return userMapper.toDto(user);
    }

    public UserResDto getUserByUsername(String username) {

        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toDto(user);
    }

    public List<UserResDto> getUsers() {

        List<User> users = userRepository.findAll();

        return userMapper.toDtoList(users);
    }

    public UserResDto updateUser(Long id, UserUpdateReqDto reqDto) {

        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (userRepository.existsByEmailAndIdNot(reqDto.email(), id)) {
            throw new UserAlreadyExistsException("A user with this email or username already exists.");
        }

        userMapper.updateEntityFromDto(reqDto, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toDto(updatedUser);
    }

    public UserResDto changePassword(Long id, ChangePasswordReqDto reqDto) {

        if (id == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (passwordEncoder.matches(user.getPassword(), reqDto.currentPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        if (!reqDto.newPassword().equals(reqDto.confirmPassword())) {
            throw new InvalidPasswordException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(reqDto.newPassword()));
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(Long id) {

        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        userRepository.deleteById(id);
    }
}

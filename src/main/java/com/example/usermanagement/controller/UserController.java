package com.example.usermanagement.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagement.dtos.user.ChangePasswordReqDto;
import com.example.usermanagement.dtos.user.UserResDto;
import com.example.usermanagement.dtos.user.UserUpdateReqDto;
import com.example.usermanagement.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResDto> getUserById(@PathVariable Long id) {

        UserResDto user = userService.getUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResDto> getUserByUsername(@PathVariable String username) {

        UserResDto user = userService.getUserByUsername(username);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResDto>> getUsers() {

        List<UserResDto> users = userService.getUsers();

        return ResponseEntity.ok().body(users);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateReqDto reqDto) {

        UserResDto user = userService.updateUser(id, reqDto);

        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<UserResDto> changePassword(
            @PathVariable Long id, @Valid @RequestBody ChangePasswordReqDto reqDto) {

        UserResDto user = userService.changePassword(id, reqDto);

        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}

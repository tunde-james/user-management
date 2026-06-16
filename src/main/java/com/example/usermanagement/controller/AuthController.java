package com.example.usermanagement.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagement.dtos.auth.LoginReqDto;
import com.example.usermanagement.dtos.auth.LoginResDto;
import com.example.usermanagement.dtos.auth.RegisterReqDto;
import com.example.usermanagement.dtos.auth.RegisterResDto;
import com.example.usermanagement.dtos.user.UserResDto;
import com.example.usermanagement.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResDto> register(@Valid @RequestBody RegisterReqDto reqDto) {

        RegisterResDto response = authService.register(reqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@Valid @RequestBody LoginReqDto reqDto) {

        LoginResDto response = authService.login(reqDto);

        ResponseCookie cookie = authService.buildAuthCookie(response.accessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/login/mobile")
    public ResponseEntity<LoginResDto> loginMobile(@Valid @RequestBody LoginReqDto reqDto) {

        LoginResDto response = authService.login(reqDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResDto> getAuthenticatedUser(Authentication authentication) {

        UserResDto user = authService.getAuthenticatedUser(authentication.getName());

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "JWT", required = false) String jwtToken) {

        ResponseCookie expiredCookie = authService.logout(jwtToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .build();
    }
}

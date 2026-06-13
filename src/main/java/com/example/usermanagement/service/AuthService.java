package com.example.usermanagement.service;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagement.config.JwtProperties;
import com.example.usermanagement.dtos.auth.LoginReqDto;
import com.example.usermanagement.dtos.auth.LoginResDto;
import com.example.usermanagement.dtos.auth.RegisterReqDto;
import com.example.usermanagement.dtos.auth.RegisterResDto;
import com.example.usermanagement.dtos.user.UserResDto;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.enums.UserRole;
import com.example.usermanagement.exception.UserAlreadyExistsException;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.security.CustomUserDetails;
import com.example.usermanagement.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthService(
            UserRepository userRepository,
            JwtService jwtService,
            JwtProperties jwtProperties,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    public RegisterResDto register(RegisterReqDto reqDto) {

        if (userRepository.existsByUsername(normalize(reqDto.username()))) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        if (userRepository.existsByEmail(normalize(reqDto.email()))) {
            throw new UserAlreadyExistsException("Email is already registered");
        }

        User user = userMapper.toEntity(reqDto);
        user.setUsername(normalize(reqDto.username()));
        user.setEmail(normalize(reqDto.email()));
        user.setPassword(passwordEncoder.encode(reqDto.password()));
        user.setRole(UserRole.ROLE_USER);

        User savedUser = userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String token = jwtService.generateToken(userDetails);

        return new RegisterResDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                token,
                "Bearer",
                jwtProperties.getExpiresIn().toSeconds());
    }

    public LoginResDto login(LoginReqDto reqDto) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(reqDto.usernameOrEmail(), reqDto.password()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new LoginResDto(token, "Bearer", jwtProperties.getExpiresIn().toSeconds());
    }

    public ResponseCookie buildAuthCookie(String token) {

        return ResponseCookie.from("JWT", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProperties.getExpiresIn().toSeconds())
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie clearAuthCookie() {

        return ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    public UserResDto getAuthenticatedUser(String username) {

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toDto(user);
    }

    private String normalize(String value) {

        return value == null ? null : value.trim().toLowerCase();
    }
}

package com.example.usermanagement.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String REGISTER_URL = "/api/v1/auth/register";
    private static final String LOGIN_URL = "/api/v1/auth/login";
    private static final String ME_URL = "/api/v1/auth/me";
    private static final String LOGOUT_URL = "/api/v1/auth/logout";

    @Test
    void fullAuthFlow_RegisterLoginAccessProtectedEndpoint_WithValidToken() throws Exception {

        String registerJson = """
            {
                "username": "testuser",
                "email": "test@test.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());

        String loginJson = """
            {
                "usernameOrEmail": "testuser",
                "password": "Password123!"
            }
            """;

        MvcResult loginResult = mockMvc.perform(
                        post(LOGIN_URL).contentType(MediaType.APPLICATION_JSON).content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        String setCookieHeader = loginResult.getResponse().getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains("JWT=");
        String jwtToken = extractToken(setCookieHeader);

        mockMvc.perform(get(ME_URL).cookie(new Cookie("JWT", jwtToken))).andExpect(status().isOk());

        mockMvc.perform(post(LOGOUT_URL).cookie(new Cookie("JWT", jwtToken))).andExpect(status().isOk());

        mockMvc.perform(get(ME_URL).cookie(new Cookie("JWT", jwtToken))).andExpect(status().isUnauthorized());
    }

    private String extractToken(String setCookieHeader) {

        return setCookieHeader.split(";")[0].replace("JWT=", "");
    }
}

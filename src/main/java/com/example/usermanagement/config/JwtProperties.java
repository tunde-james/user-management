package com.example.usermanagement.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret;
    private String issuer;
    private Duration expiresIn = Duration.ofHours(1);

    @PostConstruct
    void validate() {

        Assert.hasText(secret, "app.jwt.secret must be set and cannot be blank");

        byte[] keyBytes;

        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (DecodingException e) {
            throw new IllegalArgumentException("app.jwt.secret must be a valid Base64 string", e);
        }
        Assert.isTrue(keyBytes.length >= 32, "app.jwt.secret must decode to at least 32 bytes for HS256");
        Assert.hasText(issuer, "app.jwt.issuer must be set and cannot be blank");
        Assert.notNull(expiresIn, "app.jwt.expiration must be set");
    }
}

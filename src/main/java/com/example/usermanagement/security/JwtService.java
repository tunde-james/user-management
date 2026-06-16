package com.example.usermanagement.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import com.example.usermanagement.config.JwtProperties;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final TokenBlocklistService tokenBlocklistService;
    private SecretKey signinKey;

    public JwtService(JwtProperties jwtProperties, TokenBlocklistService tokenBlocklistService) {
        this.jwtProperties = jwtProperties;
        this.tokenBlocklistService = tokenBlocklistService;
        this.signinKey = deriveKey(jwtProperties.getSecret());
    }

    public String generateToken(UserDetails userDetails) {

        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getExpiresIn().toMillis());

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signinKey, io.jsonwebtoken.Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {

        try {
            final String username = extractUsername(jwt);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
        } catch (JwtException e) {
            return false;
        }
    }

    private SecretKey deriveKey(String secret) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {

        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signinKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

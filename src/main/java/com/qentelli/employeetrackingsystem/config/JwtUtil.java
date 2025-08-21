package com.qentelli.employeetrackingsystem.config;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private static final long EXPIRATION_MILLIS = 24 * 60 * 60 * 1000;

    @PostConstruct
    public void init() {
        byte[] secretBytes = Base64.getDecoder().decode(secret);
        this.key = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MILLIS);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        try {
            return parseClaims(token).getSubject();
        } catch (JwtException e) {
            log.warn("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    public Date extractExpiration(String token) {
        try {
            return parseClaims(token).getExpiration();
        } catch (JwtException e) {
            log.warn("Failed to extract expiration from token: {}", e.getMessage());
            return null;
        }
    }

    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            Claims claims = parseClaims(token);
            String actualUsername = claims.getSubject();
            Date expiration = claims.getExpiration();
            boolean valid = actualUsername != null && actualUsername.equals(expectedUsername)
                    && !isTokenExpired(expiration);
            if (!valid) {
                log.warn("Token validation failed for user '{}'", expectedUsername);
            }
            return valid;
        } catch (JwtException e) {
            log.warn("Token validation exception: {}", e.getMessage());
            return false;
        }
    }
}
package com.qentelli.employeetrackingsystem.config;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private final long EXPIRATION_MILLIS = 24 * 60 * 60 * 1000; // 24 hours

	public String generateToken(String email) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + EXPIRATION_MILLIS);

		return Jwts.builder().setSubject(email).setIssuedAt(now).setExpiration(expiry).signWith(key).compact();
	}

	public Claims parseClaims(String token) throws JwtException {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return parseClaims(token).getSubject();
	}

	public Date extractExpiration(String token) {
		return parseClaims(token).getExpiration();
	}

	private boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}

	public boolean isTokenValid(String token, String expectedUsername) {
		try {
			Claims claims = parseClaims(token);
			String actualUsername = claims.getSubject();
			Date expiration = claims.getExpiration();
			return actualUsername != null && actualUsername.equals(expectedUsername) && !isTokenExpired(expiration);

		} catch (JwtException e) {
			System.out.println("Token validation failed: " + e.getMessage());
			return false;
		}
	}

}
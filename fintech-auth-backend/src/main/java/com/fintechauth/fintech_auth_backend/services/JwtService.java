package com.walletly.walletly_backend.services;

import com.walletly.walletly_backend.dtos.response.UserResponse;
import com.walletly.walletly_backend.modals.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.access.expiration}")
	private String accessExpiration;
	
	@Value("${jwt.refresh.expiration}")
	private String refreshExpiration;
	
	private SecretKey getSecretKey () {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String generateAccessToken(UserResponse user) {
		return Jwts.builder()
				.subject(user.getId())
				.claim("user_email", user.getEmail())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + Long.parseLong(accessExpiration)))
				.signWith(getSecretKey())
				.compact();
	}
	
	public String generateRefreshToken (UserResponse user) {
		return Jwts.builder()
				.subject(user.getId())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + Long.parseLong(refreshExpiration)))
				.signWith(getSecretKey())
				.compact();
	}
	
	public <T> T extractClaim (String token, Function<Claims, T> function) {
		Claims claims = extractAllClaims(token);
		return function.apply(claims);
	}
	
	public Claims extractAllClaims (String token) {
		return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
	}
	
	public boolean tokenIsValid(String token, User user) {
		return !tokenIsExpired(token) && extractAllClaims(token).getSubject().equals(user.getId());
	}
	
	public Boolean tokenIsExpired (String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
}
package com.fintechauth.fintech_auth_backend.services;

import com.fintechauth.fintech_auth_backend.exceptions.*;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisHashCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;

@Service()
public class OtpService {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private MailService mailService;
	
	static final int OTP_TTL = 5;
	static final int OTP_REQUESTS_TTL = 30;
	static final int ACCOUNT_LOCK_TTL = 15;
	static final int ATTEMPTS_LIMIT = 10;
	static final int REQUESTS_LIMIT = 5;
	
	public String generateOtp () {
		SecureRandom secureRandom = new SecureRandom();
		int otpInt = secureRandom.nextInt(900000) + 100000;
		return String.valueOf(otpInt);
	}
	
	private String encodeOtp (String otp, String salt) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		String saltedOtp = otp + salt;
		
		byte[] hash = digest.digest(saltedOtp.getBytes(StandardCharsets.UTF_8));
		
		return Base64.getEncoder().encodeToString(hash);
	}
	
	public void storeOtp(String otp, String email) {
		String salt = UUID.randomUUID().toString();
		
		String encodedOtp = encodeOtp(otp, salt);
		
		Map<String, String> otpMap = Map.of("code", encodedOtp, "salt", salt);
		
		redisTemplate.opsForHash()
				.putAndExpire(
						"otp:code:salt:" + email,
						otpMap,
						RedisHashCommands.HashFieldSetOption.UPSERT,
						Expiration.from(Duration.ofMinutes(OTP_TTL))
				);
		
		// TODO: storing the salt and value in the same place??
	}
	
	public void invalidateOtp (String email) {
		redisTemplate.unlink("otp:code:salt:"+email);
	}
	
	public void resetCounter (String counterKey) {
		redisTemplate.delete(counterKey);
	}
	
	public void resetRedisOtpKeys(String email) {
		redisTemplate.unlink(List.of(
				"otp:code:salt:" + email,
				"otp:attempts:" + email,
				"otp:requests:" + email,
				"otp:requests:cooldown:" + email
				)
		);
	}
	
	public void sendOtp (String email, String otp) throws AccountLockedException, MessagingException, UnsupportedEncodingException {
		final String requestsKey = "otp:requests:" + email;
		final String requestCooldownKey = "otp:requests:cooldown:" + email;
		
		List<Object> results = redisTemplate.executePipelined(new SessionCallback<Object>() {
			@Override
			public <K, V> Object execute(@NonNull RedisOperations<K, V> operations) throws DataAccessException {
				redisTemplate.hasKey("otp:requests:locked:" + email);
				redisTemplate.hasKey("otp:attempts:locked:" + email);
				
				redisTemplate.opsForValue().set("otp:requests:" + email, "0", Expiration.from(Duration.ofMinutes(OTP_REQUESTS_TTL)));
				
				redisTemplate.opsForValue().setIfAbsent(
						requestCooldownKey,
						"1",
						Expiration.from(Duration.ofMinutes(1))
				);
				return null;
			}
		});
		
		System.out.println(results);
		
		if (Boolean.TRUE.equals(results.getFirst()) || Boolean.TRUE.equals(results.get(1))) {
			throw new AccountLockedException("Too many verification code requests");
		}
		
		Boolean createdCooldown = (Boolean) results.getLast();
		
		if (Boolean.FALSE.equals(createdCooldown)) throw new CooldownActiveException(redisTemplate.getExpire(requestCooldownKey));
		
		Long requests = redisTemplate.opsForValue().increment(requestsKey);
		
		long currentReqCount = requests == null ? 0 : requests;
		
		if (currentReqCount > REQUESTS_LIMIT) { // Used > so if a prev request incs the key this catches it
			throw new TooManyOtpRequestsException();
		}
		
			mailService.sendEmail(email, otp, "OTP Verification");
		
		if (currentReqCount == REQUESTS_LIMIT) {
			redisTemplate.opsForValue().set(
					"otp:requests:locked:" + email,
					"1",
					Expiration.from(Duration.ofMinutes(ACCOUNT_LOCK_TTL))
			);
			
			resetCounter(requestsKey);
		}
	}
	
	public void verifyOtp (String email, String otp) throws AccountLockedException {
		final String attemptsKey = "otp:attempts:" + email;
		
		List<Object> results = redisTemplate.executePipelined(new SessionCallback<Object>() {
			@Override
			public <K, V> Object execute(@NonNull RedisOperations<K, V> operations) throws DataAccessException {
				redisTemplate.hasKey(("otp:attempts:locked:" + email));
				redisTemplate.opsForValue().set(attemptsKey, "0", Expiration.from(Duration.ofMinutes(OTP_REQUESTS_TTL)));
				return null;
			}
		});
		
		if (Boolean.TRUE.equals(results.getFirst()))
			throw new AccountLockedException("Too many verification code attempts");
		
		Long attempts = redisTemplate.opsForValue().increment(attemptsKey);
		
		long currentAttemptsCount = attempts == null ? 0 : attempts;
		
		if (currentAttemptsCount > ATTEMPTS_LIMIT) {
			throw new TooManyAttemptsException();
		}
		
		if (currentAttemptsCount == ATTEMPTS_LIMIT) {
			redisTemplate.executePipelined(new SessionCallback<Object>() {
				@Override
				public <K, V> Object execute(@NonNull RedisOperations<K, V> operations) throws DataAccessException {
					redisTemplate.opsForValue().set(
							"otp:attempts:locked:" + email,
							"1",
							Expiration.from(Duration.ofMinutes(ACCOUNT_LOCK_TTL))
					);
					
					redisTemplate.opsForValue().set(
							"otp:requests:locked:" + email,
							"1",
							Expiration.from(Duration.ofMinutes(ACCOUNT_LOCK_TTL))
					);
					return null;
				}
			});
			invalidateOtp(email);
			resetCounter(attemptsKey);
			throw new TooManyAttemptsException();
		}
		
		Map<Object, Object> otpEntries = redisTemplate.opsForHash().entries("otp:code:salt:" + email);
		
		String storedOtp = (String) otpEntries.get("code");
		
		if (storedOtp == null) throw new OtpHasExpiredException();
		
		String salt = (String) otpEntries.get("salt");
		
		String encodedSentOtp = encodeOtp(otp, salt);
		
		if (!storedOtp.equals(encodedSentOtp)) {
			throw new OtpMissMatchException();
		}
	}
}

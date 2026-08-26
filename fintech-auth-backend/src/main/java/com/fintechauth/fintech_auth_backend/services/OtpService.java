package com.walletly.walletly_backend.services;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.walletly.walletly_backend.exceptions.*;
import jakarta.mail.MessagingException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.security.auth.login.AccountLockedException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
		
		redisTemplate.opsForValue().set("otp:code:"+email,encodedOtp, OTP_TTL, TimeUnit.MINUTES);
		redisTemplate.opsForValue().set("otp:salt:"+email,salt, OTP_TTL, TimeUnit.MINUTES);
		// TODO: storing the salt and value in the same place??
	}
	
	public void invalidateOtp (String email) {
		redisTemplate.delete("otp:code:"+email);
		redisTemplate.delete("otp:salt:"+email);
	}
	
	public void resetCounter (String counterKey) {
		redisTemplate.delete(counterKey);
	}
	
	public void resetRedisOtpKeys(String email) {
		redisTemplate.delete(List.of(
				"otp:code:" + email,
				"otp:salt:" + email,
				"otp:attempts:" + email,
				"otp:requests:" + email,
				"otp:requests:cooldown:" + email
				)
		);
	}
	
	public void sendOtp (String email, String otp) throws AccountLockedException, MessagingException, UnsupportedEncodingException {
		if (Boolean.TRUE.equals(redisTemplate.hasKey("otp:requests:locked:" + email))
				|| Boolean.TRUE.equals(redisTemplate.hasKey("otp:attempts:locked:" + email))) {
			throw new AccountLockedException("Too many verification code requests");
		}
		
		final String requestsKey = "otp:requests:" + email;
		final String requestCooldownKey = "otp:requests:cooldown:" + email;
		
		Boolean createdCooldown = redisTemplate.opsForValue().setIfAbsent(requestCooldownKey, "1", 1, TimeUnit.MINUTES);
		
		if (Boolean.FALSE.equals(createdCooldown)) throw new CooldownActiveException(redisTemplate.getExpire(requestCooldownKey));
		
		Long requests = redisTemplate.opsForValue().increment(requestsKey); // This creates the key and increments it
		
		long currentReqCount = requests == null ? 0 : requests;
		
		if (currentReqCount == 1) {
			redisTemplate.expire(requestsKey, OTP_REQUESTS_TTL, TimeUnit.MINUTES); // if the key was created add the TTL
		}
		
		if (currentReqCount > REQUESTS_LIMIT) { // Used > so if a prev request incs the key this catches it
			throw new TooManyOtpRequestsException();
		}
		
		
			mailService.sendEmail(email, otp, "OTP Verification");
		
		if (currentReqCount == REQUESTS_LIMIT) {
			redisTemplate.opsForValue().set("otp:requests:locked:" + email, "1", ACCOUNT_LOCK_TTL, TimeUnit.MINUTES);
			resetCounter(requestsKey);
		}
	}
	
	public void verifyOtp (String email, String otp) throws AccountLockedException {
		if (Boolean.TRUE.equals(redisTemplate.hasKey(("otp:attempts:locked:" + email))))
			throw new AccountLockedException("Too many verification code attempts");
		
		final String attemptsKey = "otp:attempts:" + email;
		
		Long attempts = redisTemplate.opsForValue().increment(attemptsKey);
		
		if (attempts != null && attempts == 1) {
			redisTemplate.expire("otp:attempts:" + email, OTP_REQUESTS_TTL, TimeUnit.MINUTES);
		}
		
		long currentAttemptsCount = attempts == null ? 0 : attempts;
		
		if (currentAttemptsCount > ATTEMPTS_LIMIT) {
			throw new TooManyAttemptsException();
		}
		
		if (currentAttemptsCount == ATTEMPTS_LIMIT) {
			redisTemplate.opsForValue().set("otp:attempts:locked:" + email, "1", ACCOUNT_LOCK_TTL, TimeUnit.MINUTES);
			redisTemplate.opsForValue().set("otp:requests:locked:" + email, "1", ACCOUNT_LOCK_TTL, TimeUnit.MINUTES);
			invalidateOtp(email);
			resetCounter(attemptsKey);
			throw new TooManyAttemptsException();
		}
		
		String storedOtp = redisTemplate.opsForValue().get("otp:code:" + email);
		
		if (storedOtp == null) throw new OtpHasExpiredException();
		
		String salt = redisTemplate.opsForValue().get("otp:salt:" + email);
		String encodedSentOtp = encodeOtp(otp, salt);
		
		System.out.println(salt);
		System.out.println(storedOtp);
		System.out.println(encodedSentOtp);
		
		System.out.println("otp valid");
		if (!storedOtp.equals(encodedSentOtp)) {
			throw new OtpMissMatchException();
		}
	}
}

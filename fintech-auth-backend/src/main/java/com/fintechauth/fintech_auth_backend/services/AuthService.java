package com.fintechauth.fintech_auth_backend.services;

import com.fintechauth.fintech_auth_backend.dtos.requests.LoginRequest;
import com.fintechauth.fintech_auth_backend.mappers.Mapper;
import com.fintechauth.fintech_auth_backend.dtos.requests.RegistrationRequest;
import com.fintechauth.fintech_auth_backend.dtos.response.UserResponse;
import com.fintechauth.fintech_auth_backend.exceptions.SessionNotFoundException;
import com.fintechauth.fintech_auth_backend.exceptions.UserEmailAlreadyExists;
import com.fintechauth.fintech_auth_backend.models.Wallet;
import com.fintechauth.fintech_auth_backend.repos.WalletRepo;
import com.fintechauth.fintech_auth_backend.security.MyUserDetails;
import com.fintechauth.fintech_auth_backend.models.User;
import com.fintechauth.fintech_auth_backend.repos.UserRepo;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.ObjectMapper;

import javax.security.auth.login.AccountLockedException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
	@Autowired
	private OtpService otpService;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private WalletRepo walletRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private MailService mailService;
	
	static final Long SESSION_TTL = 30L;
	
	public String initiateRegistration (RegistrationRequest regInfo) throws AccountLockedException {
		String regReqEmail = regInfo.getEmail();
		
		if (userRepo.existsByEmail(regReqEmail))
			throw new UserEmailAlreadyExists(regReqEmail);
		
		String id = Optional.ofNullable(getRegId(regReqEmail)).orElseGet(this::generateId);
		
		regInfo.setPassword(Objects.requireNonNull(passwordEncoder.encode(regInfo.getPassword())));
		
		storeRegSession(id, regInfo);
		
		issueOtp(regReqEmail);
		
		return id;
	}
	
	public UserResponse verifyRegistration (String otp, String sessionId) throws AccountLockedException {
		RegistrationRequest regRequest = getRegInfo(sessionId);
		
		if (regRequest == null) throw new SessionNotFoundException();
		
		User user = Mapper.regRequestToUser(regRequest);
		String userEmail = user.getEmail();
		
		otpService.verifyOtp(userEmail, otp);
		
		user.setCreatedAt(Instant.now());
		
		User savedUser = userRepo.save(user);
		
		Wallet newWallet = Mapper.mapToWallet(savedUser);
		
		walletRepo.save(newWallet);
		
		otpService.resetRedisOtpKeys(userEmail);
		resetRedisRegKeys(sessionId, userEmail);
		
		return Mapper.userToUserResponse(user);
	}
	
	public UserResponse login (LoginRequest request) {
		Authentication auth = authManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(
								request.getEmail(),
								request.getPassword()
						)
				);
		
		MyUserDetails userDetails = (MyUserDetails)auth.getPrincipal();
		assert userDetails != null;
		
		return Mapper.userToUserResponse(userDetails.getUser());
	}
	
	public void resendOtp (String id) throws AccountLockedException {
		RegistrationRequest regInfo = getRegInfo(id);
		
		if (regInfo == null) throw new SessionNotFoundException();
		
		String email = regInfo.getEmail();
		
		issueOtp(email);
	}
	
	public String forgotPassword (String email) throws AccountLockedException {
		if (!userRepo.existsByEmail(email)) throw new RuntimeException();
		
		String id = generateId();
		
		redisTemplate.opsForValue().set(
				"forgot:password:email:address:"+ id,
				email,
				Expiration.from(Duration.ofMinutes(SESSION_TTL))
		);
		
		issueOtp(email);
		
		return id;
	}
	
	public void resendPasswordOtp (String id) throws AccountLockedException {
		String email = redisTemplate.opsForValue().get("forgot:password:email:address:"+ id);
		
		issueOtp(email);
	}
	
	public String verifyOtp (String id, String otp) throws AccountLockedException {
		String email = redisTemplate.opsForValue().get("forgot:password:email:address:"+ id);
		otpService.verifyOtp(email, otp);
		
		if (email == null) throw new UsernameNotFoundException("Session not found");
		
		String resetPasswordId = generateId();
		
		redisTemplate.opsForValue().set("reset:password:email:address:" + resetPasswordId, email);
		
		redisTemplate.delete("forgot:password:email:address:"+id);
		otpService.resetRedisOtpKeys(email);
		
		return resetPasswordId;
	}
	
	public UserResponse resetPassword (String password, String id) throws AccountLockedException, MessagingException, UnsupportedEncodingException {
		String email = redisTemplate.opsForValue().get("reset:password:email:address:" + id);
		
		Optional<User> userOpt = userRepo.findByEmail(email);
		
		if (userOpt.isEmpty()) throw new RuntimeException();
		
		User user = userOpt.get();
		
		user.setPassword(Objects.requireNonNull(passwordEncoder.encode(password)));
		
		userRepo.save(user);
		
		redisTemplate.delete("reset:password:email:address:"+id);
		
		mailService.sendEmail(email, "You password has been reset", "Password Reset");
		return Mapper.userToUserResponse(user);
	}
	
	public void issueOtp (String email) throws AccountLockedException {
		String otp = otpService.generateOtp();
		
		try {
			otpService.sendOtp(email, otp);
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		otpService.storeOtp(otp, email);
	}
	
	public String generateId () {
		return UUID.randomUUID().toString();
	}
	
	public void storeRegSession (String id, RegistrationRequest regInfo) {
		redisTemplate.opsForValue().set(
				"otp:userInfo:"+ id,
				objectMapper.writeValueAsString(regInfo),
				Expiration.from(Duration.ofMinutes(SESSION_TTL))
		);
		redisTemplate.opsForValue().set(
				"otp:sessionId:"+regInfo.getEmail(),
				id,
				Expiration.from(Duration.ofMinutes(SESSION_TTL)));
	}
	
	public void resetRedisRegKeys (String id, String email) {
		redisTemplate.delete("otp:userInfo:"+ id);
		redisTemplate.delete("otp:sessionId:"+email);
	}
	
	public RegistrationRequest getRegInfo (String sessionId) {
		String userInfoJson = redisTemplate.opsForValue().get("otp:userInfo:"+sessionId);
		
		if (userInfoJson == null) throw new SessionNotFoundException();
		
		JsonParser parser = objectMapper.createParser(userInfoJson);
		RegistrationRequest userInfo = parser.readValueAs(RegistrationRequest.class);
		parser.close();
		
		return userInfo;
	}
	
	public String getRegId (String email) {
		return redisTemplate.opsForValue().get("otp:sessionId:"+email);
	}
}
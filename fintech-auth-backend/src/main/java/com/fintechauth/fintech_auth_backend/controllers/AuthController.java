package com.fintechauth.fintech_auth_backend.controllers;

import com.fintechauth.fintech_auth_backend.dtos.requests.LoginRequest;
import com.fintechauth.fintech_auth_backend.dtos.requests.RegistrationRequest;
import com.fintechauth.fintech_auth_backend.dtos.requests.VerifyEmailRequest;
import com.fintechauth.fintech_auth_backend.dtos.response.UserResponse;
import com.fintechauth.fintech_auth_backend.exceptions.SessionNotFoundException;
import com.fintechauth.fintech_auth_backend.services.AuthService;
import com.fintechauth.fintech_auth_backend.services.JwtService;
import com.fintechauth.fintech_auth_backend.utils.CookieType;
import com.fintechauth.fintech_auth_backend.utils.CookiesUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.security.auth.login.AccountLockedException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/auth")
@RestController

public class AuthController {
	@Autowired
	AuthService authService;
	@Autowired
	JwtService jwtService;
	
	@PostMapping("/registration/initiate")
	public ResponseEntity<?> initiateRegistration (@Valid @RequestBody RegistrationRequest regRequest) throws SessionNotFoundException, AccountLockedException {
		String registrationSessionId = authService.initiateRegistration(regRequest);
		ResponseCookie regSessionIdCookie = CookiesUtil.createCookie(CookieType.REGISTRATION_SESSION_ID, registrationSessionId);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, regSessionIdCookie.toString())
				.build();
	}
	
	@PostMapping("/registration/verify")
	public ResponseEntity<@NonNull UserResponse> verifyRegistration (@Valid @RequestBody VerifyEmailRequest verificationRequest, HttpServletRequest request) throws AccountLockedException {
		Cookie registrationIdCookie = WebUtils.getCookie(request, CookieType.REGISTRATION_SESSION_ID.getName());
		
		if (registrationIdCookie == null) throw new SessionNotFoundException();
		
		UserResponse userResponse = authService.verifyRegistration(verificationRequest.getOtp(), registrationIdCookie.getValue());
		
		ResponseCookie accessTokenCookie = CookiesUtil.createJwtCookies(CookieType.ACCESS_TOKEN, jwtService.generateAccessToken(userResponse));
		ResponseCookie refreshTokenCookie = CookiesUtil.createJwtCookies( CookieType.REFRESH_TOKEN, jwtService.generateRefreshToken(userResponse));
		
		ResponseCookie removedRegIdCookie = CookiesUtil.deleteCookie(CookieType.REGISTRATION_SESSION_ID, "");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, removedRegIdCookie.toString())
				.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.build();
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login (@RequestBody LoginRequest request) {
		UserResponse userResponse = authService.login(request);
		
		ResponseCookie accessTokenCookie = CookiesUtil.createJwtCookies(CookieType.ACCESS_TOKEN, jwtService.generateAccessToken(userResponse));
		ResponseCookie refreshTokenCookie = CookiesUtil.createJwtCookies( CookieType.REFRESH_TOKEN, jwtService.generateRefreshToken(userResponse));
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.build();
	}
	
	@PostMapping("/registration/resend-otp")
	public ResponseEntity<?> resendOtp (HttpServletRequest request) throws AccountLockedException {
		Cookie regIdCookie = WebUtils.getCookie(request, CookieType.REGISTRATION_SESSION_ID.getName());
		
		if (regIdCookie == null) throw new SessionNotFoundException();
		
		authService.resendOtp(regIdCookie.getValue());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword (@RequestBody Map<String, String> userEmail, HttpServletRequest request) throws AccountLockedException {
		Cookie forgotPasswordIdCookie = WebUtils.getCookie(request, CookieType.FORGOT_PASSWORD_SESSION_ID.getName());
		
		String cookie = (forgotPasswordIdCookie != null ? forgotPasswordIdCookie.getValue() : null);
		
		String forgotPasswordId = authService.forgotPassword(userEmail.get("email"), cookie);
		
		if (forgotPasswordIdCookie == null) {
			ResponseCookie currentForgotPasswordIdCookie = CookiesUtil.createCookie(CookieType.FORGOT_PASSWORD_SESSION_ID, forgotPasswordId);
			
			return ResponseEntity.ok()
					.header(HttpHeaders.SET_COOKIE, currentForgotPasswordIdCookie.toString())
					.build();
		}
		
		return ResponseEntity.ok()
				.build();
	}
	
	@PostMapping("/resend-otp")
	public ResponseEntity<?> resendPasswordOtp (HttpServletRequest request) throws AccountLockedException {
		Cookie forgotPasswordIdCookie = WebUtils.getCookie(request, "FORGOT_PASSWORD_SESSION_ID");
		if (forgotPasswordIdCookie == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		authService.resendPasswordOtp(forgotPasswordIdCookie.getValue());
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/verify-password-reset-otp")
	public ResponseEntity<?> verifyPasswordResetOtp (@RequestBody HashMap<String, String> otpMap, HttpServletRequest request, HttpServletResponse response) throws AccountLockedException {
		Cookie forgotPasswordIdCookie = WebUtils.getCookie(request, "FORGOT_PASSWORD_SESSION_ID");
		if (forgotPasswordIdCookie == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String resetPasswordId = authService.verifyPasswordResetOtp(forgotPasswordIdCookie.getValue(), otpMap.get("otp"));
		
		ResponseCookie resetPasswordCookie = CookiesUtil.createCookie(CookieType.RESET_PASSWORD_SESSION_ID, resetPasswordId);
		
		ResponseCookie removedForgotPasswordIdCookie = CookiesUtil.deleteCookie(CookieType.FORGOT_PASSWORD_SESSION_ID, "");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, resetPasswordCookie.toString())
				.header(HttpHeaders.SET_COOKIE, removedForgotPasswordIdCookie.toString())
				.build();
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword (@RequestBody Map<String, String> passwordMap, HttpServletRequest request, HttpServletResponse response) throws AccountLockedException, MessagingException, UnsupportedEncodingException {
		Cookie resetPasswordIdCookie = WebUtils.getCookie(request, CookieType.RESET_PASSWORD_SESSION_ID.getName());
		
		if (resetPasswordIdCookie == null) throw new RuntimeException();
		
		UserResponse userResponse = authService.resetPassword(passwordMap.get("password"), resetPasswordIdCookie.getValue());
		
		ResponseCookie removedResetPasswordIdCookie = CookiesUtil.deleteCookie(CookieType.RESET_PASSWORD_SESSION_ID, "");
		
		ResponseCookie accessTokenCookie = CookiesUtil.createJwtCookies(CookieType.ACCESS_TOKEN, jwtService.generateAccessToken(userResponse));
		ResponseCookie refreshTokenCookie = CookiesUtil.createJwtCookies( CookieType.REFRESH_TOKEN, jwtService.generateRefreshToken(userResponse));
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, removedResetPasswordIdCookie.toString())
				.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.build();
	}
	
	@GetMapping("/cookie-check")
	public ResponseEntity<?> cookieCheck (HttpServletRequest request) {
		Cookie accessTokenCookie = WebUtils.getCookie(request, CookieType.ACCESS_TOKEN.getName());
		
		System.out.println(accessTokenCookie.getValue());
		
		return ResponseEntity.ok(accessTokenCookie.getValue());
	}
}

/*
	RESET PASSWORD FLOW
	1. User inputs email and sends it to /forgot-password
	
	2. /forgot-password:
	 - generates an id
	 - stores user email in redis with id as key
	 - issues otp
	 - stores id in cookies with name FORGOT_PASSWORD_SESSION_ID
	 
	3. Redirect user to email verification page
	
	4. User sends otp to /verify-otp
	
	5. /verify-otp
		- gets FORGOT_PASSWORD_SESSION_ID from cookies
		- uses id to get email from redis
		- verifies the otp with the email
		- generates an id
		- stores user email in redis with id as key
		- deletes otp related keys and forgot password key from redis
		- stores id in cookies with name RESET_PASSWORD_SESSION_ID
		- expire FORGOT_PASSWORD_SESSION_ID cookie
		
	6. redirect user to reset password page
	
	7. user sends new password to /reset-password
	
	8. /reset-password
		- gets RESET_PASSWORD_SESSION_ID from cookies
		- uses id to get email from redis
		- find user in DB using the email
		- set new password
		- save user
		- delete reset password key from redis
		- expire RESET_PASSWORD_SESSION_ID cookie
		- log user in by creating access token and storing in cookie
*/
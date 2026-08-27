package com.fintechauth.fintech_auth_backend.controllers;

import com.fintechauth.fintech_auth_backend.services.AccountService;
import com.fintechauth.fintech_auth_backend.services.JwtService;
import com.fintechauth.fintech_auth_backend.utils.CookieType;
import com.fintechauth.fintech_auth_backend.utils.CookiesUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	@Autowired
	AccountService accountService;
	@Autowired
	JwtService jwtService;
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout () {
		ResponseCookie accessTokenCookie = CookiesUtil.deleteCookie(CookieType.ACCESS_TOKEN, "");
		ResponseCookie refreshTokenCookie = CookiesUtil.deleteCookie(CookieType.REFRESH_TOKEN, "");
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.build();
	}
	
	@DeleteMapping("delete-account")
	public ResponseEntity<?> deleteAccount (HttpServletRequest request) {
		ResponseCookie accessTokenCookie = CookiesUtil.deleteCookie(CookieType.ACCESS_TOKEN, "");
		ResponseCookie refreshTokenCookie = CookiesUtil.deleteCookie(CookieType.REFRESH_TOKEN, "");
		
		String accessToken = accessTokenCookie.getValue();
		
		String userId = jwtService.extractClaim(accessToken, Claims::getSubject);
		
		accountService.deleteAccount(userId);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.build();
	}
}

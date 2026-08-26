package com.fintechauth.fintech_auth_backend.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookiesUtil {
	public static ResponseCookie createJwtCookies (CookieType type, String jwtValue) {
		return ResponseCookie.from(type.getName(), jwtValue)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.sameSite("Strict")
				.maxAge(type.getMaxAgeSeconds())
				.build();
	}
	
	public static ResponseCookie deleteJwtCookies (CookieType type, String jwtValue) {
		return ResponseCookie.from(type.getName(), jwtValue)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.sameSite("Strict")
				.maxAge(0)
				.build();
	}
	
	public static ResponseCookie createCookie (CookieType type, String value) {
		return ResponseCookie.from(type.getName(), value)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(type.getMaxAgeSeconds())
				.sameSite("Lax")
				.build();
	}
	
	public static ResponseCookie deleteCookie (CookieType type, String value) {
		return ResponseCookie.from(type.getName(), value)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(0)
				.sameSite("Lax")
				.build();
	}
}

package com.walletly.walletly_backend.security;

import com.walletly.walletly_backend.services.JwtService;
import com.walletly.walletly_backend.utils.CookieType;
import com.walletly.walletly_backend.utils.CookiesUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	JwtService jwtService;
	@Autowired
	MyUserDetailsService userDetailsService;
	@Autowired
	@Qualifier("handlerExceptionResolver")
	HandlerExceptionResolver handlerResolver;
	
	@NullMarked
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			Cookie tokenCookie = WebUtils.getCookie(request, CookieType.ACCESS_TOKEN.getName());
			String userId = null;
			String jwtToken = null;
			String userEmail = null;
			
			if (tokenCookie != null) {
				jwtToken = tokenCookie.getValue();
				userId = jwtService.extractClaim(jwtToken, Claims::getSubject);
				userEmail = jwtService.extractClaim(jwtToken, (claims) -> claims.get("user_email", String.class));
			}
			
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				MyUserDetails userDetails = (MyUserDetails)userDetailsService.loadUserByUsername(userEmail);
				if (jwtService.tokenIsValid(jwtToken, userDetails.getUser())) {
					UsernamePasswordAuthenticationToken userToken =
							new UsernamePasswordAuthenticationToken(userDetails, null, null);
					
					userToken.setDetails(userDetails);
					
					SecurityContextHolder.getContext()
							.setAuthentication(userToken);
				}
			}
			
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			handlerResolver.resolveException(request, response, null, e);
		}
	}
}

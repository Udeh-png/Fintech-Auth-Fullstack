package com.walletly.walletly_backend.configurations;

import com.walletly.walletly_backend.security.JwtFilter;
import com.walletly.walletly_backend.security.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	MyUserDetailsService userDetailsService;
	@Autowired
	AuthenticationConfiguration authConfig;
	@Autowired
	JwtFilter jwtFilter;
	
	@Bean
	public UrlBasedCorsConfigurationSource corsConfig () {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of("http://localhost:3000", "chrome-extension://eipdnjedkpcnlmmdfdkgfpljanehloah"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		
		source.registerCorsConfiguration("/**", config);
		
		return source;
	}
	
	@Bean
	public SecurityFilterChain filterChain (HttpSecurity security) {
		security
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/**", "/api/webhooks/**")
						.permitAll()
						.anyRequest()
						.authenticated()
				).cors((configurer) -> configurer.configurationSource(corsConfig()))
				.csrf(CsrfConfigurer::disable)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		return security.build();
	}
	
	@Bean
	public AuthenticationProvider authProvider () {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
	public AuthenticationManager authManager () {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder () {
		return new BCryptPasswordEncoder(12);
	}
}

package com.fintechauth.fintech_auth_backend.security;

import com.fintechauth.fintech_auth_backend.models.User;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {
	@Getter
	User user;
	
	public MyUserDetails (User user) {
		this.user = user;
	}
	@Override
	@NullMarked
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}
	
	@Override
	public @Nullable String getPassword() {
		return user.getPassword();
	}
	
	@Override
	@NullMarked
	public String getUsername() {
		return user.getEmail();
	}
}

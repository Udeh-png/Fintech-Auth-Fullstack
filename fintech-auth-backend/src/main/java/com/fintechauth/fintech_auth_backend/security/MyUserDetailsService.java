package com.fintechauth.fintech_auth_backend.security;

import com.fintechauth.fintech_auth_backend.repos.UserRepo;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	UserRepo repo;
	
	@Override
	@NullMarked
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		return repo.findByEmail(userEmail)
				.map(MyUserDetails::new)
				.orElseThrow(
						() -> new UsernameNotFoundException("Wrong Credentials")
				);
	}
}

package com.fintechauth.fintech_auth_backend.services;

import com.fintechauth.fintech_auth_backend.models.User;
import com.fintechauth.fintech_auth_backend.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
	@Autowired
	UserRepo userRepo;
	
	public void deleteAccount (String userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		
		User user = userOpt.orElseThrow();
		
		userRepo.delete(user);
	}
}

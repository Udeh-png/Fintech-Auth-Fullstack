package com.fintechauth.fintech_auth_backend.services;

import com.fintechauth.fintech_auth_backend.dtos.response.UserResponse;
import com.fintechauth.fintech_auth_backend.dtos.response.WalletResponse;
import com.fintechauth.fintech_auth_backend.mappers.Mapper;
import com.fintechauth.fintech_auth_backend.models.User;
import com.fintechauth.fintech_auth_backend.models.Wallet;
import com.fintechauth.fintech_auth_backend.repos.UserRepo;
import com.fintechauth.fintech_auth_backend.repos.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
	@Autowired
	UserRepo userRepo;
	@Autowired
	WalletRepo walletRepo;
	
	public void deleteAccount (String userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		Optional<Wallet> walletOpt = walletRepo.findByUserId(userId);
		
		User user = userOpt.orElseThrow();
		Wallet wallet = walletOpt.orElseThrow();
		
		userRepo.delete(user);
		walletRepo.delete(wallet);
	}
	
	public UserResponse getUserDeets(String userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		
		assert userOpt.isPresent();
		
		User user = userOpt.get();
		
		return Mapper.userToUserResponse(user);
	}
	
	public WalletResponse getWalletDeets(String userId) {
		Optional<Wallet> walletOpt = walletRepo.findByUserId(userId);
		
		assert walletOpt.isPresent();
		
		Wallet wallet = walletOpt.get();
		
		return Mapper.walletToWalletResponse(wallet);
	}
}

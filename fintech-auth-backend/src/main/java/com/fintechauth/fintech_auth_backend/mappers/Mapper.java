package com.fintechauth.fintech_auth_backend.mappers;

import com.fintechauth.fintech_auth_backend.dtos.requests.RegistrationRequest;
import com.fintechauth.fintech_auth_backend.dtos.response.UserResponse;
import com.fintechauth.fintech_auth_backend.dtos.response.WalletResponse;
import com.fintechauth.fintech_auth_backend.models.User;
import com.fintechauth.fintech_auth_backend.models.Wallet;
import lombok.NonNull;

import java.time.Instant;
import java.util.Random;

public class Mapper {
	public static UserResponse userToUserResponse (@NonNull User user) {
		return new UserResponse(
				user.getId(),
				user.getUserName(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getCreatedAt()
		);
	}
	
	public static User regRequestToUser (@NonNull RegistrationRequest regReq) {
		return new User(
				regReq.getFirstName() + " " + regReq.getLastName(),
				regReq.getFirstName(),
				regReq.getLastName(),
				regReq.getEmail(),
				regReq.getPassword()
		);
	}
	
	public static Wallet mapToWallet (@NonNull User user) {
		String accNum = String.valueOf(new Random().nextLong(10000000000L));
		String[] bankNames = {"Wema", "Access", "Opay", "Moniepoint", "Kuda", "UBA"};
		int bankNameSelectionIdx = (int) accNum.charAt(5) % 5;
		return new Wallet(
				user.getId(),
				user.getUserName(),
				0,
				accNum,
				bankNames[bankNameSelectionIdx],
				user.getEmail(),
				"Nigeria",
				"ACTIVE",
				Instant.now()
		);
	}
	
	public static WalletResponse walletToWalletResponse(Wallet wallet) {
		return new WalletResponse(
				wallet.getId(),
				wallet.getAccountNumber(),
				wallet.getVirtualAccountBank(),
				wallet.getAccountName()
		);
	}
}

package com.walletly.walletly_backend.exceptions;

public class UserEmailAlreadyExists extends RuntimeException {
	
	public UserEmailAlreadyExists(String email) {
		super("User with email " + email + " already exists");
	}
	
}

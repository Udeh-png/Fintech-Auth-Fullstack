package com.fintechauth.fintech_auth_backend.exceptions;

public class UserEmailAlreadyExists extends RuntimeException {
	
	public UserEmailAlreadyExists(String email) {
		super("User with email " + email + " already exists");
	}
	
}

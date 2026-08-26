package com.fintechauth.fintech_auth_backend.exceptions;

public class SessionNotFoundException extends RuntimeException {
	public SessionNotFoundException() {
		super("Your info is not in memory");
	}
}

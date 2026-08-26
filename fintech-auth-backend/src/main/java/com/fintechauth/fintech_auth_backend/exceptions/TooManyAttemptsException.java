package com.fintechauth.fintech_auth_backend.exceptions;

public class TooManyAttemptsException extends RuntimeException {
	public TooManyAttemptsException() {
		super("Too many verification codes attempts");
	}
}

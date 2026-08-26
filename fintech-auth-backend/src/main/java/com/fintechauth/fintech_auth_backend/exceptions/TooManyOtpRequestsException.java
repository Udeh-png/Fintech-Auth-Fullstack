package com.fintechauth.fintech_auth_backend.exceptions;

public class TooManyOtpRequestsException extends RuntimeException {
	public TooManyOtpRequestsException () {
		super("Too many verification codes requested");
	}
}

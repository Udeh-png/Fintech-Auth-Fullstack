package com.fintechauth.fintech_auth_backend.exceptions;

public class OtpMissMatchException extends RuntimeException {
	public OtpMissMatchException() {
		super("The otp you sent did not match");
	}
}

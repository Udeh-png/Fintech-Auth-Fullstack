package com.fintechauth.fintech_auth_backend.exceptions;

public class OtpSessionStillActiveException extends RuntimeException {
	public OtpSessionStillActiveException() {
		super("Otp session is still active! Check you email, dumbass.");
	}
}

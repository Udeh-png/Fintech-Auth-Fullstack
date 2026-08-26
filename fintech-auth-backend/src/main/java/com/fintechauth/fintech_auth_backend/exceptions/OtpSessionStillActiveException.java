package com.walletly.walletly_backend.exceptions;

public class OtpSessionStillActiveException extends RuntimeException {
	public OtpSessionStillActiveException() {
		super("Otp session is still active! Check you email, dumbass.");
	}
}

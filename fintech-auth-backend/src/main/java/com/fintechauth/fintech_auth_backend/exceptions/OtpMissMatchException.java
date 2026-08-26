package com.walletly.walletly_backend.exceptions;

public class OtpMissMatchException extends RuntimeException {
	public OtpMissMatchException() {
		super("The otp you sent did not match");
	}
}

package com.walletly.walletly_backend.exceptions;

public class OtpHasExpiredException extends RuntimeException {
	public OtpHasExpiredException() {
		super("The otp you sent has expired mate");
	}
}

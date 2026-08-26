package com.walletly.walletly_backend.exceptions;

public class CooldownActiveException extends RuntimeException {
	
	public CooldownActiveException(Long timeLeft) {
		super("An OTP was recently sent. You can request a new one in " + timeLeft + " seconds.");
	}
}

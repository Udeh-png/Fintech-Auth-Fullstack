package com.walletly.walletly_backend.exceptions;

public class SessionNotFoundException extends RuntimeException {
	public SessionNotFoundException() {
		super("Your info is not in memory");
	}
}

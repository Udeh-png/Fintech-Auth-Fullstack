package com.walletly.walletly_backend.exceptions;

public class TooManyAttemptsException extends RuntimeException {
	public TooManyAttemptsException() {
		super("Too many verification codes attempts");
	}
}

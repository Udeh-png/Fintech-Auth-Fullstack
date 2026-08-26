package com.walletly.walletly_backend.utils;

import lombok.*;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class OtpSession {
	
	final int OTP_EXPIRATION_TIME = 60000 * 5;
	
	@NonNull
	@Setter
	private String otp;
	
	private int attempts = 0;
	
	@NonNull
	@Setter
	private Long generateTimestamp;
	
	public boolean hasExceededAttemptLimit () {
		return attempts > 5;
	}
	
	public void incrementAttempts () {
		this.attempts++;
	}
	
	public boolean hasExpired () {
		return System.currentTimeMillis() - this.getGenerateTimestamp() >= OTP_EXPIRATION_TIME;
	}
}

package com.fintechauth.fintech_auth_backend.utils;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class SuspendedAccount {
	@NonNull private String email;
	@NonNull private Long suspensionTimestamp;
	@NonNull private Long suspensionDuration;
	@NonNull private String message;
	@NonNull private Throwable reason;
	private Long suspendedUntil;
	
	public Boolean suspensionExpired () {
		return System.currentTimeMillis() > getSuspendedUntil();
	}
	
	public Long getTimeLeft () {
		return getSuspendedUntil() - System.currentTimeMillis();
	}
	
	public Long getSuspendedUntil() {
		return suspensionTimestamp + suspensionDuration;
	}
}

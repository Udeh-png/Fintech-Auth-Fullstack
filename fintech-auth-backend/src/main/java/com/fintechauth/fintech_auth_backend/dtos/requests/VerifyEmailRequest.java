package com.fintechauth.fintech_auth_backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class VerifyEmailRequest {
	@NotBlank
	@Length(min = 6, max = 6)
	private String otp;
}

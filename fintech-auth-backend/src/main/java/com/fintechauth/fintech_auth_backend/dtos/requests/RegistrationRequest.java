package com.fintechauth.fintech_auth_backend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor

@Data
public class RegistrationRequest {
	
	@NotBlank
	@Size(min=3, max = 20)
	@NonNull
	@Setter
	private String firstName;
	
	@NotBlank
	@Size(min=3, max = 20)
	@NonNull
	@Setter
	private String lastName;
	
	@NotBlank
	@Email
	@NonNull
	@Setter
	private String email;
	
	@NotBlank
	@Size(min=8)
//	@Pattern(regexp = "^([A-Z])([a-z])([0-9])([^A-Za-z0-9])")
	@NonNull
	@Setter
	private String password;
}

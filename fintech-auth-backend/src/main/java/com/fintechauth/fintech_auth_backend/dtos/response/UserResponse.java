package com.walletly.walletly_backend.dtos.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private String id;
	
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
	
	@Setter
	private Instant createdAt;
}

package com.fintechauth.fintech_auth_backend.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Document("Users")
public class User {
	@Id
	private String id;
	
	@NonNull
	@Setter
	private String userName;
	
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
	@NonNull
	@Setter
	private String password;
	
	@Setter
	private String phone;
	
	@Setter
	private Instant createdAt;
}

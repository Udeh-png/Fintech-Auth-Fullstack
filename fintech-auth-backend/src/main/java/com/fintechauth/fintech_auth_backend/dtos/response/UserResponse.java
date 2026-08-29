package com.fintechauth.fintech_auth_backend.dtos.response;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private String id;
	
	private String userName;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private Instant createdAt;
}

package com.fintechauth.fintech_auth_backend.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document("Wallets")
@RequiredArgsConstructor
@Getter
public class Wallet {
	@Id
	private String id;
	
	@Setter
	@NonNull
	@Indexed(unique = true)
	private String userId;
	
	@Setter
	@NonNull
	private String accountName;
	
	@Setter
	@NonNull
	private double balance;
	
	@Setter
	@NonNull
	private String virtualAccountNumber;
	
	@Setter
	@NonNull
	private String virtualAccountBank;
	
	@Setter
	@NonNull
	private String email;
	
	@Setter
	private String mobileNumber;
	
	@Setter
	@NonNull
	private String country;
	
	@Setter
	@NonNull
	private String status;
	
	@Setter
	@NonNull
	private Instant createdAt;
}

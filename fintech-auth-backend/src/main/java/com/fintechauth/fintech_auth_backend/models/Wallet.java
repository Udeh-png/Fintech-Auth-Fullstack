package com.fintechauth.fintech_auth_backend.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document("Wallets")
@RequiredArgsConstructor
@Data
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
	private String accountNumber;
	
	@Setter
	@NonNull
	private String virtualAccountBank;
	
	@Setter
	@NonNull
	private String emailAddress;
	
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

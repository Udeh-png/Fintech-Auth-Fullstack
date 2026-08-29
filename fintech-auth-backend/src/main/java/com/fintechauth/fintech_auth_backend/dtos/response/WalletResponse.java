package com.fintechauth.fintech_auth_backend.dtos.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {
	String id;
	String accountNumber;
	String bankName;
	String accountName;
}

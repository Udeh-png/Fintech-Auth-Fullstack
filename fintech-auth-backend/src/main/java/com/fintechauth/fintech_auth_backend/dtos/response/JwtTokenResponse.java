package com.walletly.walletly_backend.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenResponse {
	private String accessToken;
	private String refreshToken;
	private String type;
}

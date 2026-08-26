package com.walletly.walletly_backend.dtos.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {
	private String message;
	private String type;
}

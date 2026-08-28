package com.fintechauth.fintech_auth_backend.configurations;

import com.resend.Resend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResendConfig {
	@Value("&{resend.api.key}")
	String resendApiKey;
	
	String apiKey;
	public Resend resend () {
		return new Resend(resendApiKey);
	}
}

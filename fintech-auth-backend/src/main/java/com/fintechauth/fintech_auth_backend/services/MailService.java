package com.fintechauth.fintech_auth_backend.services;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.resend.core.exception.ResendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class MailService {
	
	@Autowired
	private Resend resend;
	
	@Value("${resend.from.email}")
	private String fromEmail;
	
	public void sendEmail(String toEmail,  String content, String subject) {
		CreateEmailOptions params = CreateEmailOptions.builder()
				.from("Fintech-Auth-Demo <" + fromEmail + ">")
				.to(toEmail)
				.subject(subject)
				.html(buildOtpHtml(content))
				.build();
		
		try {
			CreateEmailResponse response = resend.emails().send(params);
			// response.getId() gives you the Resend message ID if you want to log it
		} catch (ResendException e) {
			// Replace with your actual logger (e.g. SLF4J)
			throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
		}
	}
	
	private String buildOtpHtml(String otpCode) {
		return """
                <div style="font-family: sans-serif; padding: 20px;">
                    <h2>Your Verification Code</h2>
                    <p>Use the code below to complete your action:</p>
                    <h1 style="letter-spacing: 4px;">%s</h1>
                    <p>This code expires in 5 minutes. If you didn't request this, ignore this email.</p>
                </div>
                """.formatted(otpCode);
	}
}
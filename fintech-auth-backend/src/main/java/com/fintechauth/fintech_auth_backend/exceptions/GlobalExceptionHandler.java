package com.fintechauth.fintech_auth_backend.exceptions;

import com.fintechauth.fintech_auth_backend.dtos.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountLockedException;
import java.net.UnknownHostException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserEmailAlreadyExists.class)
	public ResponseEntity<@NonNull ErrorResponse> handleEmailExist (UserEmailAlreadyExists eae) {
		ErrorResponse er = new ErrorResponse(eae.getMessage(),"EMAIL_EXISTS");
		
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(er);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<@NonNull ErrorResponse> handleBadCredentials (BadCredentialsException bc) {
		ErrorResponse er = new ErrorResponse("Invalid Email or Password.","BAD_CREDENTIALS");
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(er);
	}
	
	@ExceptionHandler(UnknownHostException.class)
	public ResponseEntity<@NonNull ErrorResponse> unknownHostHandler (UnknownHostException uhe) {
		ErrorResponse er = new ErrorResponse("The connection is taking too long. Please check your internet and try again.", "NETWORK");
		return ResponseEntity
				.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(er);
	}
	
	@ExceptionHandler(OtpMissMatchException.class)
	public ResponseEntity<@NonNull ErrorResponse> wrongOtpHandler (OtpMissMatchException ome) {
		ErrorResponse er = new ErrorResponse(ome.getMessage(), "OTP_MISMATCH");
		
		return ResponseEntity.badRequest().body(er);
	}
	
	@ExceptionHandler(OtpHasExpiredException.class)
	public ResponseEntity<@NonNull ErrorResponse> otpHasExpired (OtpHasExpiredException ohe) {
		ErrorResponse er = new ErrorResponse(ohe.getMessage(), "EXPIRED_OTP");
		
		return ResponseEntity.status(HttpStatus.GONE).body(er);
	}
	
	@ExceptionHandler(SessionNotFoundException.class)
	public ResponseEntity<@NonNull ErrorResponse> userSessionNotFound (SessionNotFoundException snf) {
		ErrorResponse er = new ErrorResponse(snf.getMessage(), "SESSION_NOT_FOUND");
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<@NonNull ErrorResponse> invalidHandlerArg (MethodArgumentNotValidException manv) {
		ErrorResponse er = new ErrorResponse("Bad Request Nigga!!!" + manv.getMessage(), "BAD_REQUEST");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
	}
	
	@ExceptionHandler(OtpSessionStillActiveException.class)
	public ResponseEntity<@NonNull ErrorResponse> otpStillActive (OtpSessionStillActiveException ossa) {
		ErrorResponse er = new ErrorResponse(ossa.getMessage(), "SESSION_STILL_ACTIVE");
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
	}
	
	@ExceptionHandler(AccountLockedException.class)
	public ResponseEntity<@NonNull ErrorResponse> otpStillActive (AccountLockedException ossa) {
		ErrorResponse er = new ErrorResponse(ossa.getMessage(), "ACCOUNT_LOCKED");
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
	}
	
	@ExceptionHandler(CooldownActiveException.class)
	public ResponseEntity<@NonNull ErrorResponse> cooldownActive (CooldownActiveException ca) {
		ErrorResponse er = new ErrorResponse(ca.getMessage(), "COOLDOWN_ACTIVE");
		
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(er);
	}
	
	@ExceptionHandler(TooManyOtpRequestsException.class)
	public ResponseEntity<@NonNull ErrorResponse> tooManyRequests (TooManyOtpRequestsException tmr) {
		ErrorResponse er = new ErrorResponse(tmr.getMessage(), "TOO_MANY_REQUESTS");
		
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(er);
	}
	
	@ExceptionHandler(TooManyAttemptsException.class)
	public ResponseEntity<@NonNull ErrorResponse> tooManyAttempts (TooManyAttemptsException tma) {
		ErrorResponse er = new ErrorResponse(tma.getMessage(), "TOO_MANY_REQUESTS");
		
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(er);
	}
	
	@ExceptionHandler (UsernameNotFoundException.class)
	public ResponseEntity<@NonNull ErrorResponse> usernameNotFound (UsernameNotFoundException unf) {
		ErrorResponse er = new ErrorResponse(unf.getMessage(), "WRONG_CREDENTIALS");
		log.error("Bad request: ", unf);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
	}
	
	@ExceptionHandler (ExpiredJwtException.class)
	public ResponseEntity<@NonNull ErrorResponse> expiredJwt (ExpiredJwtException ej) {
		ErrorResponse er = new ErrorResponse(ej.getMessage(), "EXPIRED_JWT");
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(er);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<@NonNull ErrorResponse> handleAllUnhandledExceptions(Exception ex) {
		ErrorResponse er = new ErrorResponse("An unexpected error occurred. Please try again later.", "UNKNOWN");
		
		// Log the actual error internally so developers can fix it
		log.error("Unhandled exception caught: ", ex);
		
		return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
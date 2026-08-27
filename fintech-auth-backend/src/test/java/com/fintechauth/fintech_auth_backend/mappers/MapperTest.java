package com.fintechauth.fintech_auth_backend.mappers;

import com.fintechauth.fintech_auth_backend.models.User;
import com.fintechauth.fintech_auth_backend.models.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {
	
	@Test
	void mapToWallet() {
		User user = new User("Udeh Chisom", "Chisom", "Udeh", "leonwokedichisom@gmail.com", "password");
		user.setId("121221");
		Wallet wallet = Mapper.mapToWallet(user);
		assertEquals(10, wallet.getAccountName().length());
	}
}
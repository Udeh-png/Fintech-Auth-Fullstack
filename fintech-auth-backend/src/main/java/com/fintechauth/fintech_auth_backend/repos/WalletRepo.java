package com.fintechauth.fintech_auth_backend.repos;

import com.fintechauth.fintech_auth_backend.models.Wallet;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepo extends MongoRepository<@NonNull  Wallet, @NonNull String> {
	Optional<Wallet> findByUserId(String userId);
	
}

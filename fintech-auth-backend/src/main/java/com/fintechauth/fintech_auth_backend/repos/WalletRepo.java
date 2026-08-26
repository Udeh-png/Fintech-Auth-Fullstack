package com.walletly.walletly_backend.repos;

import com.walletly.walletly_backend.modals.Wallet;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepo extends MongoRepository<@NonNull  Wallet, @NonNull String> {
	Optional<Wallet> findByUserId(String userId);
	
	Optional<Wallet> findByAccountNumber(String accountNumber);
	
	Optional<Wallet> findByMobileNumber(String mobileNumber);
	
	Optional<Wallet> findByEmailAddress(String emailAddress);
	
}

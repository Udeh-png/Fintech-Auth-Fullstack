package com.walletly.walletly_backend.repos;

import com.walletly.walletly_backend.modals.User;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepo extends MongoRepository<@NonNull User, @NonNull String> {

	Boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
}

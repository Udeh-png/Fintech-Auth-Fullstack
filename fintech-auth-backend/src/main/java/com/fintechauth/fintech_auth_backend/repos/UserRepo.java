package com.fintechauth.fintech_auth_backend.repos;

import com.fintechauth.fintech_auth_backend.models.User;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepo extends MongoRepository<@NonNull User, @NonNull String> {

	Boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
}

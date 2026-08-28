package com.fintechauth.fintech_auth_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.redis.autoconfigure.DataRedisRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = { DataRedisRepositoriesAutoConfiguration.class })
public class FintechAuthBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FintechAuthBackendApplication.class, args);
	}

}

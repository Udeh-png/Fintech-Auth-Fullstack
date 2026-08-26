package com.walletly.walletly_backend.security;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ClientSideConfig;
import io.github.bucket4j.distributed.proxy.ExecutionStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.api.StatefulRedisConnection;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

public class RateLimitFilter extends OncePerRequestFilter {
	@Autowired
	StatefulRedisConnection<String, byte[]> redisConnection;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LettuceBasedProxyManager<String> proxyManager = LettuceBasedProxyManager
				.builderFor(redisConnection)
				.withClientSideConfig(
						ClientSideConfig
								.getDefault()
								.withExecutionStrategy(
										(ExecutionStrategy) ExpirationAfterWriteStrategy
												.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10))
								)
				)
				.build();
		
		BucketConfiguration bucketConfig = BucketConfiguration
				.builder()
				.addLimit(
						limit -> limit.capacity(1_000)
								.refillIntervally(1_000, Duration.ofMinutes(10))
				).build();
		
		Bucket bucket = proxyManager.builder().build("", () -> bucketConfig);
	}
}

package com.example.gateway.route;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class CustomeRoutes {

	@Bean
	RouteLocator customeRoutesBuilder(RouteLocatorBuilder routeLocatorBuilder) {

		return routeLocatorBuilder.routes()
				.route(p -> p.path("/users/**")
						.filters(f -> f.rewritePath("/users/(?<segment>.)", "/${segment}"))
//								.circuitBreaker(c -> c.setName("userCircuitBreaker").setFallbackUri("/contactSupport")))
						.uri("lb://USERS"))
				.route(p -> p.path("/movies/**")
						.filters(f -> f.rewritePath("/movies/(?<segment>.)", "/${segment}")
								.circuitBreaker(c -> c.setName("movieRepositoryCircuitBreaker").setFallbackUri("/contactSupport")))
						.uri("lb://MOVIEREPOSITORY"))
				.route(p -> p.path("/movie-ratings/**")
						.filters(f -> f.rewritePath("/movie-ratings/(?<segment>.)", "/${segment}"))
//								.circuitBreaker(c -> c.setName("movieRatingCircuitBreaker").setFallbackUri("/contactSupport")))
						.uri("lb://MOVIERATE"))
				.route(p -> p.path("/favorite-movies/**")
						.filters(f -> f.rewritePath("/favorite-movies/(?<segment>.)", "/${segment}"))
//								.circuitBreaker(c -> c.setName("movieFavCircuitBreaker").setFallbackUri("/contactSupport")))
						.uri("lb://MOVIEFAV"))
				.build();
	}

	@Bean
	Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

}

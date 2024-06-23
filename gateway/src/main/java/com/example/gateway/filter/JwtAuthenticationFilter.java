package com.example.gateway.filter;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.gateway.constant.Constant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class JwtAuthenticationFilter implements GlobalFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String jwt = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if ((jwt == null || !jwt.startsWith("Bearer ")) && (request.getPath().toString().contains("/login")
				|| request.getPath().toString().contains("/register"))) {
			return chain.filter(exchange);
		}

		try {
			jwt = jwt.substring(7);
			SecretKey key = Keys.hmacShaKeyFor(Constant.JWT_KEY.getBytes(StandardCharsets.UTF_8));
			Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
			String username = String.valueOf(claims.get("username"));

			ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header("X-Username", username).build();
			exchange = exchange.mutate().request(modifiedRequest).build();
			return chain.filter(exchange);
		} catch (Exception e) {
			System.out.println(e);
		}

		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}

}
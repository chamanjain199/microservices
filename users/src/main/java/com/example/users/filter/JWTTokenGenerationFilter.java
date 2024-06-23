package com.example.users.filter;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.users.constant.Constant;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenGenerationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, java.io.IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication!=null && authentication.isAuthenticated()) {
			SecretKey key = Keys.hmacShaKeyFor(Constant.JWT_KEY.getBytes(StandardCharsets.UTF_8));
			String jwt = Jwts.builder().claim("issuer", authentication.getName()).claim("issuer", "Chaman Jain")
					.claim("subject", "JWT token").claim("username", authentication.getName())
					.claim("issuedAt", new Date()).claim("issuedAt", new Date())
					.expiration(new Date(new Date().getTime() + 300000000)).signWith(key).compact();
			response.setHeader(Constant.AUTHENTICATION, jwt);
		}
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getServletPath();
		return (path.equals("/login"));
	}

}
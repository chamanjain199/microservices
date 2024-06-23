package com.example.movie.fav.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.movie.fav.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				(requests) -> requests.requestMatchers("/public", "/error","/actuator/**").permitAll().anyRequest().authenticated());
		http.csrf(x -> x.disable());
		http.addFilterBefore(new JwtAuthenticationFilter(), BasicAuthenticationFilter.class);
		return http.build();
	}
}
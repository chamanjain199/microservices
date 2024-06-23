package com.example.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.users.filter.JWTTokenGenerationFilter;
import com.example.users.filter.JWTAuthenticationFilter;

@Configuration
@EnableWebSecurity()
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests.requestMatchers("/error", "/api/user/register","/actuator/**","/h2-console/**").permitAll()
				.anyRequest().authenticated()).httpBasic(z -> {
				});
		http.csrf(x -> x.disable());
		http.addFilterAfter(new JWTTokenGenerationFilter(), BasicAuthenticationFilter.class);
		http.addFilterBefore(new JWTAuthenticationFilter(), BasicAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

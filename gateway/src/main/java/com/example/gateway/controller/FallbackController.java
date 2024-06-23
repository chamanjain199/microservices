package com.example.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
	@PostMapping("/contactSupport")
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Mono<String> fallBack() {
		return Mono.just("Some internal error accured , please contact to admin");
	}
}

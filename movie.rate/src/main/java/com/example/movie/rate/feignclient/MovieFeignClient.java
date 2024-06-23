package com.example.movie.rate.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "MOVIEREPOSITORY",fallback = MovieFeignClientFallback.class)
public interface MovieFeignClient {
	@GetMapping("/api/movie/exist/{id}")
	public ResponseEntity<Boolean> existById(@RequestHeader("X-Username") String username, @PathVariable Long id);
}

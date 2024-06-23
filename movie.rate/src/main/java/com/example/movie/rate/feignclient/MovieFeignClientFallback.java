package com.example.movie.rate.feignclient;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MovieFeignClientFallback implements MovieFeignClient {

	@Override
	public ResponseEntity<Boolean> existById(String username, Long id) {
		// TODO Auto-generated method stub
		return ResponseEntity.of(Optional.of(Boolean.FALSE));
	}

}

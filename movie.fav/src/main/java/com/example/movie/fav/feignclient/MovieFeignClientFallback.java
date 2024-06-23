package com.example.movie.fav.feignclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.movie.fav.dto.MovieDTO;

@Component
public class MovieFeignClientFallback implements MovieFeignClient {

	@Override
	public ResponseEntity<Boolean> existById(String username, Long id) {
		// TODO Auto-generated method stub
		return ResponseEntity.of(Optional.of(Boolean.FALSE));
	}

	@Override
	public ResponseEntity<List<MovieDTO>> getAllMoviesByIds(String username, List<Long> ids) {
		// TODO Auto-generated method stub
		return ResponseEntity.of(Optional.of(new ArrayList<>()));
	}

}

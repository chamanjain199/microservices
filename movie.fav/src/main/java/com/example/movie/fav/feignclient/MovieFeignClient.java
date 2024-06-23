package com.example.movie.fav.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.movie.fav.dto.MovieDTO;

@FeignClient(name = "MOVIEREPOSITORY", fallback = MovieFeignClientFallback.class)
public interface MovieFeignClient {
	@GetMapping("/api/movie/exist/{id}")
	public ResponseEntity<Boolean> existById(@RequestHeader("X-Username") String username, @PathVariable("id") Long id);

	@GetMapping("/api/movie/byids")
	public ResponseEntity<List<MovieDTO>> getAllMoviesByIds(@RequestHeader("X-Username") String username,
			@RequestParam("ids") List<Long> ids);
}

package com.example.movie.rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.rate.dto.MovieRatingDTO;
import com.example.movie.rate.service.MovieRatingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/movie-rating")
public class MovieRatingController {

	@Autowired
	private MovieRatingService movieRatingService;

	@Operation(summary = "Create a new movie rating")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie rating created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
	@PostMapping
	public ResponseEntity<MovieRatingDTO> createMovieRating(@RequestBody @Valid MovieRatingDTO movieRatingDTO) {
		return new ResponseEntity<MovieRatingDTO>(movieRatingService.createMovieRating(movieRatingDTO),
				HttpStatus.CREATED);
	}

	@Operation(summary = "Delete a movie rating")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie rating deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Movie rating not found") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMovieRating(@PathVariable Long id) {
		movieRatingService.deleteMovieRating(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Get a movie rating by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie rating retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "Movie rating not found") })
	@GetMapping("/{id}")
	public ResponseEntity<MovieRatingDTO> getMovieRatingById(@PathVariable @NotNull Long id) {
		return new ResponseEntity<MovieRatingDTO>(movieRatingService.getMovieRatingById(id), HttpStatus.OK);
	}

	@Operation(summary = "Get all movie ratings by movie ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie ratings retrieved successfully") })
	@GetMapping
	public ResponseEntity<Page<MovieRatingDTO>> getAllMovieRatings(@RequestParam @NotNull Long movieId,
			Pageable pageable) {
		return new ResponseEntity<>(movieRatingService.getAllMovieRatings(movieId, pageable), HttpStatus.OK);
	}
}
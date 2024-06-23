package com.example.movie.fav.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.fav.dto.FavoriteMovieDTO;
import com.example.movie.fav.dto.MovieDTO;
import com.example.movie.fav.service.FavoriteMovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/movie-fav")
public class FavoriteMovieController {

	@Autowired
	private FavoriteMovieService favoriteMovieService;

	@Operation(summary = "Favorite a movie")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie favorited successfully."),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
	@PostMapping
	public ResponseEntity<FavoriteMovieDTO> favoriteAMovie(@RequestBody @Valid FavoriteMovieDTO favoriteMovieDTO) {
		return new ResponseEntity<FavoriteMovieDTO>(favoriteMovieService.createFavoriteMovie(favoriteMovieDTO),
				HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Unfavorite a movie")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie unfavorited successfully."),
			@ApiResponse(responseCode = "404", description = "No favorited movie found") })
	@DeleteMapping()
	public ResponseEntity<Void> unfavoriteAMovie(
			@RequestParam(required = true, name = "userId") @NotNull(message = "Please provide valid user id") Long userId,
			@RequestParam(required = true, name = "movieId") @NotNull(message = "Please provide valid movie id") Long movieId) {
		favoriteMovieService.deleteFavoriteMovie(userId, movieId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}

	@Operation(summary = "Get all favorited movie  by user ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Favorited movie retrieved successfully") })
	@GetMapping
	public ResponseEntity<Page<MovieDTO>> getAllFavoriteMovieOfUser(
			@RequestParam(required = true, name = "userId") @NotNull(message = "Please provide valid user id") Long userId,
			Pageable pageable) {
		return new ResponseEntity<Page<MovieDTO>>(favoriteMovieService.getAllFavoriteMovie(userId, pageable),
				HttpStatus.OK);
	}
}
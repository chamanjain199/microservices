package com.example.movie.repository.controller;

import java.util.List;

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

import com.example.movie.repository.dto.MovieDTO;
import com.example.movie.repository.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@Operation(summary = "Create a new movie")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Movie created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MovieDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PostMapping
	public ResponseEntity<MovieDTO> createMovie(@RequestBody @Valid MovieDTO movieDTO) {
		MovieDTO createdMovie = movieService.createMovie(movieDTO);
		return new ResponseEntity<>(createdMovie, HttpStatus.CREATED);
	}

	@Operation(summary = "Get a movie by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movie found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MovieDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Movie not found", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
		MovieDTO movieDTO = movieService.getMovieById(id);
		return new ResponseEntity<>(movieDTO, HttpStatus.OK);
	}

	@Operation(summary = "Get movie page")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found all movie", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)) }) })
	@GetMapping
	public ResponseEntity<Page<MovieDTO>> getAllMovies(Pageable pageable) {
		Page<MovieDTO> movies = movieService.getAllMovies(pageable);
		return new ResponseEntity<>(movies, HttpStatus.OK);
	}

	@Operation(summary = "Delete a movie")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Movie deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "Movie not found", content = @Content) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMovie(@PathVariable @NotNull Long id) {
		movieService.deleteMovie(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Check if a movie exist by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Movie found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)) }),
			@ApiResponse(responseCode = "404", description = "Movie not found", content = @Content) })
	@GetMapping("/exist/{id}")
	public ResponseEntity<Boolean> existById(@PathVariable Long id) {
		return new ResponseEntity<>(movieService.existById(id), HttpStatus.OK);
	}

	@Operation(summary = "Get all movies by ids")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found all movie by ids ", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MovieDTO.class))) }) })
	@GetMapping("/byids")
	public ResponseEntity<List<MovieDTO>> getAllMoviesByIds(@RequestParam("ids") List<Long> ids) {
		List<MovieDTO> movies = movieService.getAllMoviesByIds(ids);
		return new ResponseEntity<>(movies, HttpStatus.OK);
	}

}
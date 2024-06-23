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

import com.example.movie.rate.dto.MovieCommentDTO;
import com.example.movie.rate.service.MovieCommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/movie-comment")
public class MovieCommentController {

	@Autowired
	private MovieCommentService movieCommentService;

	@Operation(summary = "Create a new movie comment")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie comment created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
	@PostMapping
	public ResponseEntity<MovieCommentDTO> createMovieComment(@RequestBody @Valid MovieCommentDTO movieCommentDTO) {
		return new ResponseEntity<MovieCommentDTO>(movieCommentService.createMovieComment(movieCommentDTO),
				HttpStatus.CREATED);
	}

	@Operation(summary = "Delete a movie comment")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie comment deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Movie comment not found") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMovieComment(@PathVariable Long id) {
		movieCommentService.deleteMovieComment(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Operation(summary = "Get a movie comment by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie comment retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "Movie comment not found") })
	@GetMapping("/{id}")
	public ResponseEntity<MovieCommentDTO> getMovieCommentById(
			@PathVariable @NotNull(message = "Please provide valid comment id") Long id) {
		return new ResponseEntity<>(movieCommentService.getMovieCommentById(id), HttpStatus.OK);
	}

	@Operation(summary = "Get all movie comments by movie ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Movie comments retrieved successfully") })
	@GetMapping
	public ResponseEntity<Page<MovieCommentDTO>> getAllMovieComments(
			@RequestParam(required = true) @NotNull(message = "Please provide valid movie id") Long movieId,
			Pageable pageable) {
		return new ResponseEntity<Page<MovieCommentDTO>>(movieCommentService.getAllMovieComments(movieId, pageable),
				HttpStatus.OK);
	}
}
package com.example.movie.fav.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public class FavoriteMovieDTO {

	private Long id;

	@NotNull(message = "Please provide movie id")
	private Long movieId;
	@NotNull(message = "Please provide user id")
	private Long userId;
	private LocalDateTime createdDate = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
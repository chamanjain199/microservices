package com.example.movie.fav.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class FavoriteMovie {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fav_movie_seq_gen")
	@SequenceGenerator(initialValue = 1, sequenceName = "fav_movie_seq", name = "fav_movie_seq_gen")
	private Long id;

	private long movieId;

	private Long userId;

	private LocalDateTime createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getMovieId() {
		return movieId;
	}

	public void setMovieId(long movieId) {
		this.movieId = movieId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}



}

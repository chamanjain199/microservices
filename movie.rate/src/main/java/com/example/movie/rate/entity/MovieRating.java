package com.example.movie.rate.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MovieRating {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_rating_seq_gen")
	@SequenceGenerator(initialValue = 1, sequenceName = "movie_rating_seq", name = "movie_rating_seq_gen")
	private Long id;

	private int rating;

	private long movieId;

	private String rateBy;

	private LocalDateTime rateDate;

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

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getRateBy() {
		return rateBy;
	}

	public void setRateBy(String rateBy) {
		this.rateBy = rateBy;
	}

	public LocalDateTime getRateDate() {
		return rateDate;
	}

	public void setRateDate(LocalDateTime rateDate) {
		this.rateDate = rateDate;
	}

}

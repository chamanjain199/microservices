package com.example.movie.rate.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MovieRatingDTO {

    private Long id;
    @NotNull(message = "Please rate")
    @Min(value = 1,message = "Min rating is 1")
    @Max(value = 5,message = "Max rating is 5")
    private Integer rating;
    @NotNull(message = "Please provide movie id")
    private Long movieId;
    @NotNull(message = "Rateby can not be null")
    private String rateBy;
    private LocalDateTime rateDate = LocalDateTime.now();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public long getMovieId() {
		return movieId;
	}
	public void setMovieId(long movieId) {
		this.movieId = movieId;
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
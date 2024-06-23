package com.example.movie.rate.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.movie.rate.dto.MovieRatingDTO;

public interface MovieRatingService {

	MovieRatingDTO createMovieRating(MovieRatingDTO movieRatingDTO);

	void deleteMovieRating(Long id);

	MovieRatingDTO getMovieRatingById(Long id);

	Page<MovieRatingDTO> getAllMovieRatings(Long movieId, Pageable pageable);
}

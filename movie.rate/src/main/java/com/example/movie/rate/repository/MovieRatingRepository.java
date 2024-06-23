package com.example.movie.rate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.movie.rate.entity.MovieRating;

public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {

	Page<MovieRating> findAllByMovieId(long movieId, Pageable pageable);
	
	@Query("select round( avg(rating))  from MovieRating where movieId=:movieId ")
	Double findRatingOfMovie(long movieId);
}
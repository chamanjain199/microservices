package com.example.movie.fav.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.movie.fav.dto.FavoriteMovieDTO;
import com.example.movie.fav.dto.MovieDTO;

public interface FavoriteMovieService {

	FavoriteMovieDTO createFavoriteMovie(FavoriteMovieDTO favoriteMovieDTO);

	void deleteFavoriteMovie(Long userId, Long movieId);

	Page<MovieDTO> getAllFavoriteMovie(Long userId, Pageable pageable);
}
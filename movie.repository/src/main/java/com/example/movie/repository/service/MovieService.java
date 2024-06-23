package com.example.movie.repository.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.movie.repository.dto.MovieDTO;

public interface MovieService {

	MovieDTO createMovie(MovieDTO movieDTO);

	MovieDTO getMovieById(Long id);

	Page<MovieDTO> getAllMovies(Pageable pageable);

	void deleteMovie(Long id);

	boolean existById(Long id);

	List<MovieDTO> getAllMoviesByIds(List<Long> ids);
}
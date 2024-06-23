package com.example.movie.rate.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.movie.rate.dto.MovieCommentDTO;

public interface MovieCommentService {

	MovieCommentDTO createMovieComment(MovieCommentDTO movieCommentDTO);

	void deleteMovieComment(Long id);

	MovieCommentDTO getMovieCommentById(Long id);

	Page<MovieCommentDTO> getAllMovieComments(Long movieId,Pageable pageable);
}
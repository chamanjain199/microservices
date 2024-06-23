package com.example.movie.rate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.movie.rate.dto.MovieCommentDTO;
import com.example.movie.rate.entity.MovieComment;
import com.example.movie.rate.exception.MovieCommnetNotFoundException;
import com.example.movie.rate.exception.MovieNotFoundException;
import com.example.movie.rate.feignclient.MovieFeignClient;
import com.example.movie.rate.mapper.MovieCommentMapper;
import com.example.movie.rate.repository.MovieCommentRepository;
import com.example.movie.rate.service.MovieCommentService;

@Service
public class MovieCommentServiceImpl implements MovieCommentService {

	@Autowired
	private MovieCommentRepository movieCommentRepository;

	@Autowired
	private MovieFeignClient movieFeignClient;

	@Override
	public MovieCommentDTO createMovieComment(MovieCommentDTO movieCommentDTO) {

		ResponseEntity<Boolean> doesMovieExist = movieFeignClient.existById(
				SecurityContextHolder.getContext().getAuthentication().getName(), movieCommentDTO.getMovieId());
		if (!doesMovieExist.getBody()) {
			throw new MovieNotFoundException("Movie not found");
		}
		MovieComment movieComment = MovieCommentMapper.toEntity(movieCommentDTO);
		movieComment = movieCommentRepository.save(movieComment);
		return MovieCommentMapper.toDto(movieComment);
	}

	@Override
	public void deleteMovieComment(Long id) {
		if (!movieCommentRepository.existsById(id)) {
			throw new MovieCommnetNotFoundException("MovieComment not found");
		}
		movieCommentRepository.deleteById(id);
	}

	@Override
	public MovieCommentDTO getMovieCommentById(Long id) {
		MovieComment movieComment = movieCommentRepository.findById(id)
				.orElseThrow(() -> new MovieCommnetNotFoundException("MovieComment not found"));
		return MovieCommentMapper.toDto(movieComment);
	}

	@Override
	public Page<MovieCommentDTO> getAllMovieComments(Long movieId, Pageable pageable) {
		Page<MovieComment> page = movieCommentRepository.findAllByMovieId(movieId, pageable);
		return new PageImpl<>(MovieCommentMapper.toDtoList(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}
}

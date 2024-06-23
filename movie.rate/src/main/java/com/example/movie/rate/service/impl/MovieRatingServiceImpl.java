package com.example.movie.rate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.movie.rate.constant.Constant;
import com.example.movie.rate.dto.MovieRatingDTO;
import com.example.movie.rate.entity.MovieRating;
import com.example.movie.rate.exception.MovieNotFoundException;
import com.example.movie.rate.exception.MovieRatingNotFoundException;
import com.example.movie.rate.feignclient.MovieFeignClient;
import com.example.movie.rate.mapper.MovieRatingMapper;
import com.example.movie.rate.repository.MovieRatingRepository;
import com.example.movie.rate.service.MovieRatingService;

@Service
public class MovieRatingServiceImpl implements MovieRatingService {

	@Autowired
	private MovieRatingRepository movieRatingRepository;
	@Autowired
	private MovieFeignClient movieFeignClient;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public MovieRatingDTO createMovieRating(MovieRatingDTO movieRatingDTO) {
		ResponseEntity<Boolean> doesMovieExist = movieFeignClient.existById(
				SecurityContextHolder.getContext().getAuthentication().getName(), movieRatingDTO.getMovieId());
		if (!doesMovieExist.getBody()) {
			throw new MovieNotFoundException("Movie not found");
		}
		MovieRating movieRating = MovieRatingMapper.toEntity(movieRatingDTO);
		movieRating = movieRatingRepository.saveAndFlush(movieRating);
		Integer rating = calculateLatestRating(movieRatingDTO.getMovieId()).intValue();
		kafkaTemplate.send(Constant.KAFKA_TOPIC_RATE_MOVIE, String.valueOf(movieRating.getMovieId()),
				movieRating.getMovieId() + ":" + rating);
		return MovieRatingMapper.toDto(movieRating);
	}

	@Override
	public void deleteMovieRating(Long id) {
		if (!movieRatingRepository.existsById(id)) {
			throw new MovieRatingNotFoundException("MovieRating not found");
		}
		movieRatingRepository.deleteById(id);
	}

	@Override
	public MovieRatingDTO getMovieRatingById(Long id) {
		MovieRating movieRating = movieRatingRepository.findById(id)
				.orElseThrow(() -> new MovieRatingNotFoundException("MovieRating not found"));
		return MovieRatingMapper.toDto(movieRating);
	}

	@Override
	public Page<MovieRatingDTO> getAllMovieRatings(Long movieId, Pageable pageable) {
		Page<MovieRating> page = movieRatingRepository.findAllByMovieId(movieId, pageable);
		return new PageImpl<MovieRatingDTO>(MovieRatingMapper.toDtoList(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	private Double calculateLatestRating(long movieId) {
		return movieRatingRepository.findRatingOfMovie(movieId);
	}
}

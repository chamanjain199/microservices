package com.example.movie.repository.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.movie.repository.constant.Constant;
import com.example.movie.repository.dto.MovieDTO;
//import com.example.movie.repository.elasticsearch.repository.MovieDocumentRepository;
import com.example.movie.repository.entity.Movie;
import com.example.movie.repository.mapper.MovieMapper;
import com.example.movie.repository.repository.MovieRepository;
import com.example.movie.repository.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MovieRepository movieRepository;
	
//	@Autowired
//	private MovieDocumentRepository movieDocumentRepository;


	@Override
	public MovieDTO createMovie(MovieDTO movieDTO) {
		Movie movie = MovieMapper.toEntity(movieDTO);
		movie = movieRepository.save(movie);
		//movieDocumentRepository.save(MovieMapper.toMovieDocument(movie));
		return MovieMapper.toDto(movie);
	}

	@Override
	public MovieDTO getMovieById(Long id) {
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
		return MovieMapper.toDto(movie);
	}

	@Override
	public Page<MovieDTO> getAllMovies(Pageable pageable) {
		Page<Movie> moviePage = movieRepository.findAll(pageable);
		return new PageImpl<>(MovieMapper.toDtoList(moviePage.toList()), moviePage.getPageable(),
				moviePage.getTotalElements());

	}

	@Override
	public void deleteMovie(Long id) {
		if (!movieRepository.existsById(id)) {
			throw new RuntimeException("Movie not found");
		}
		//movieDocumentRepository.deleteById(id);
		movieRepository.deleteById(id);
	}

	@Override
	public boolean existById(Long id) {
		return movieRepository.existsById(id);
	}

	@Override
	public List<MovieDTO> getAllMoviesByIds(List<Long> ids) {
		return MovieMapper.toDtoList(movieRepository.findAllById(ids));
	}

	@KafkaListener(groupId = "rate", topics = Constant.KAFKA_TOPIC_RATE_MOVIE)
	public void listen(String data) {

		String[] split = data.split(":");
		Long movieId = Long.valueOf(split[0]);
		int rating = Integer.valueOf(split[1]);
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
		movie.setRating(rating);
		movieRepository.save(movie);
		logger.info("Movie " + movie.getName() + " rating is updated " + rating+" "+data);
	}
}
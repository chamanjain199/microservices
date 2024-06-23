package com.example.movie.repository.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.example.movie.repository.dto.MovieDTO;
import com.example.movie.repository.entity.Movie;

public class MovieMapper {

	public static MovieDTO toDto(Movie movie) {
		if (movie == null) {
			return null;
		}

		MovieDTO movieDTO = new MovieDTO();
		movieDTO.setId(movie.getId());
		movieDTO.setName(movie.getName());
		movieDTO.setReleaseYear(movie.getReleaseYear());
		movieDTO.setGenre(movie.getGenre());
		movieDTO.setLanguage(movie.getLanguage());
		movieDTO.setLengthInMin(movie.getLengthInMin());
		movieDTO.setRating(movie.getRating());
		movieDTO.setDescription(movie.getDescription());

		return movieDTO;
	}
	
//	public static MovieDocument toMovieDocument(Movie movie) {
//		if (movie == null) {
//			return null;
//		}
//
//		MovieDocument movieDocument = new MovieDocument();
//		movieDocument.setId(movie.getId());
//		movieDocument.setName(movie.getName());
//		movieDocument.setReleaseYear(movie.getReleaseYear());
//		movieDocument.setGenre(movie.getGenre());
//		movieDocument.setLanguage(movie.getLanguage());
//		movieDocument.setLengthInMin(movie.getLengthInMin());
//		movieDocument.setRating(movie.getRating());
//		movieDocument.setDescription(movie.getDescription());
//
//		return movieDocument;
//	}

	public static Movie toEntity(MovieDTO movieDTO) {
		if (movieDTO == null) {
			return null;
		}

		Movie movie = new Movie();
		movie.setId(movieDTO.getId());
		movie.setName(movieDTO.getName());
		movie.setReleaseYear(movieDTO.getReleaseYear());
		movie.setGenre(movieDTO.getGenre());
		movie.setLanguage(movieDTO.getLanguage());
		movie.setLengthInMin(movieDTO.getLengthInMin());
		movie.setRating(movieDTO.getRating());
		movie.setDescription(movieDTO.getDescription());

		return movie;
	}

	public static List<MovieDTO> toDtoList(Collection<Movie> movies) {
		return movies.stream().map(MovieMapper::toDto).collect(Collectors.toList());
	}

	public static List<Movie> toEntityList(Collection<MovieDTO> dtos) {
		return dtos.stream().map(MovieMapper::toEntity).collect(Collectors.toList());
	}

}
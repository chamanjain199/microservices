package com.example.movie.rate.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.example.movie.rate.dto.MovieRatingDTO;
import com.example.movie.rate.entity.MovieRating;

public class MovieRatingMapper {

	public static MovieRatingDTO toDto(MovieRating movieRating) {
		if (movieRating == null) {
			return null;
		}

		MovieRatingDTO dto = new MovieRatingDTO();
		dto.setId(movieRating.getId());
		dto.setRating(movieRating.getRating());
		dto.setMovieId(movieRating.getMovieId());
		dto.setRateBy(movieRating.getRateBy());
		dto.setRateDate(movieRating.getRateDate());

		return dto;
	}

	public static MovieRating toEntity(MovieRatingDTO dto) {
		if (dto == null) {
			return null;
		}

		MovieRating movieRating = new MovieRating();
		movieRating.setId(dto.getId());
		movieRating.setRating(dto.getRating());
		movieRating.setMovieId(dto.getMovieId());
		movieRating.setRateBy(dto.getRateBy());
		movieRating.setRateDate(dto.getRateDate());

		return movieRating;
	}

	public static List<MovieRatingDTO> toDtoList(Collection<MovieRating> movies) {
		return movies.stream().map(MovieRatingMapper::toDto).collect(Collectors.toList());
	}

	public static List<MovieRating> toEntityList(Collection<MovieRatingDTO> dtos) {
		return dtos.stream().map(MovieRatingMapper::toEntity).collect(Collectors.toList());
	}
}

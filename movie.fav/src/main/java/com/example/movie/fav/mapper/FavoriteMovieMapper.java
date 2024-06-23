package com.example.movie.fav.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.example.movie.fav.dto.FavoriteMovieDTO;
import com.example.movie.fav.entity.FavoriteMovie;

public class FavoriteMovieMapper {

	public static FavoriteMovieDTO toDto(FavoriteMovie entity) {
		if (entity == null) {
			return null;
		}

		FavoriteMovieDTO dto = new FavoriteMovieDTO();
		dto.setId(entity.getId());
		dto.setMovieId(entity.getMovieId());
		dto.setUserId(entity.getUserId());
		dto.setCreatedDate(entity.getCreatedDate());

		return dto;
	}

	public static FavoriteMovie toEntity(FavoriteMovieDTO dto) {
		if (dto == null) {
			return null;
		}

		FavoriteMovie favoriteMovie = new FavoriteMovie();
		favoriteMovie.setId(dto.getId());
		favoriteMovie.setMovieId(dto.getMovieId());
		favoriteMovie.setUserId(dto.getUserId());
		favoriteMovie.setCreatedDate(dto.getCreatedDate());

		return favoriteMovie;
	}

	public static List<FavoriteMovieDTO> toDtoList(Collection<FavoriteMovie> movies) {
		return movies.stream().map(FavoriteMovieMapper::toDto).collect(Collectors.toList());
	}

	public static List<FavoriteMovie> toEntityList(Collection<FavoriteMovieDTO> dtos) {
		return dtos.stream().map(FavoriteMovieMapper::toEntity).collect(Collectors.toList());
	}
}

package com.example.movie.rate.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.example.movie.rate.dto.MovieCommentDTO;
import com.example.movie.rate.entity.MovieComment;

public class MovieCommentMapper {

	public static MovieCommentDTO toDto(MovieComment movieComment) {
		if (movieComment == null) {
			return null;
		}

		MovieCommentDTO dto = new MovieCommentDTO();
		dto.setId(movieComment.getId());
		dto.setComment(movieComment.getComment());
		dto.setMovieId(movieComment.getMovieId());
		dto.setCommnendBy(movieComment.getCommnendBy());
		dto.setCommentDate(movieComment.getCommentDate());

		return dto;
	}

	public static MovieComment toEntity(MovieCommentDTO dto) {
		if (dto == null) {
			return null;
		}

		MovieComment movieComment = new MovieComment();
		movieComment.setId(dto.getId());
		movieComment.setComment(dto.getComment());
		movieComment.setMovieId(dto.getMovieId());
		movieComment.setCommnendBy(dto.getCommnendBy());
		movieComment.setCommentDate(dto.getCommentDate());

		return movieComment;
	}

	public static List<MovieCommentDTO> toDtoList(Collection<MovieComment> movies) {
		return movies.stream().map(MovieCommentMapper::toDto).collect(Collectors.toList());
	}

	public static List<MovieComment> toEntityList(Collection<MovieCommentDTO> dtos) {
		return dtos.stream().map(MovieCommentMapper::toEntity).collect(Collectors.toList());
	}
}

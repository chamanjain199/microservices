package com.example.movie.rate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movie.rate.entity.MovieComment;

public interface MovieCommentRepository extends JpaRepository<MovieComment, Long> {
	Page<MovieComment> findAllByMovieId(long movieId, Pageable pageable);
}
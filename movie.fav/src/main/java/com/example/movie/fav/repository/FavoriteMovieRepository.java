package com.example.movie.fav.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.movie.fav.entity.FavoriteMovie;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {

	@Query("select movieId from FavoriteMovie where userId=:userId")
	Page<Long> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
	
	boolean existsByUserIdAndMovieId(Long userId, long movieId);
	FavoriteMovie findByUserIdAndMovieId(Long userId, long movieId);

}
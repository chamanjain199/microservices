package com.example.movie.fav.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.movie.fav.dto.FavoriteMovieDTO;
import com.example.movie.fav.dto.MovieDTO;
import com.example.movie.fav.entity.FavoriteMovie;
import com.example.movie.fav.feignclient.MovieFeignClient;
import com.example.movie.fav.mapper.FavoriteMovieMapper;
import com.example.movie.fav.repository.FavoriteMovieRepository;
import com.example.movie.fav.service.FavoriteMovieService;

@Service
public class FavoriteMovieServiceImpl implements FavoriteMovieService {

	@Autowired
	private FavoriteMovieRepository favoriteMovieRepository;

	@Autowired
	private MovieFeignClient movieFeignClient;

	@Override
	public FavoriteMovieDTO createFavoriteMovie(FavoriteMovieDTO favoriteMovieDTO) {
		Boolean alreadyFavorited = favoriteMovieRepository.existsByUserIdAndMovieId(favoriteMovieDTO.getUserId(),
				favoriteMovieDTO.getMovieId());
		if (alreadyFavorited) {
			throw new RuntimeException("Movie is already favorited");

		}
		ResponseEntity<Boolean> doesMovieExist = movieFeignClient.existById(
				SecurityContextHolder.getContext().getAuthentication().getName(), favoriteMovieDTO.getMovieId());
		if (!doesMovieExist.getBody()) {
			throw new RuntimeException("Movie not found");
		}
		FavoriteMovie favoriteMovie = FavoriteMovieMapper.toEntity(favoriteMovieDTO);
		favoriteMovie = favoriteMovieRepository.save(favoriteMovie);
		return FavoriteMovieMapper.toDto(favoriteMovie);
	}

	@Override
	public void deleteFavoriteMovie(Long userId, Long movieId) {
		FavoriteMovie alreadyFavorited = favoriteMovieRepository.findByUserIdAndMovieId(userId, movieId);
		if (alreadyFavorited == null) {
			throw new RuntimeException("this movie is not your favorited movie");
		}

		favoriteMovieRepository.deleteById(alreadyFavorited.getId());
	}

	@Override
	public Page<MovieDTO> getAllFavoriteMovie(Long userId, Pageable pageable) {
		Page<Long> movieIdPage = favoriteMovieRepository.findAllByUserId(userId, pageable);
		return new PageImpl<>(
				movieFeignClient.getAllMoviesByIds(SecurityContextHolder.getContext().getAuthentication().getName(),
						movieIdPage.getContent()).getBody(),
				movieIdPage.getPageable(), movieIdPage.getTotalElements());
	}
}

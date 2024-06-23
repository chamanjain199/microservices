package com.example.movie.fav.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import com.example.movie.fav.dto.FavoriteMovieDTO;
import com.example.movie.fav.dto.MovieDTO;
import com.example.movie.fav.entity.FavoriteMovie;
import com.example.movie.fav.feignclient.MovieFeignClient;
import com.example.movie.fav.repository.FavoriteMovieRepository;
import com.example.movie.fav.service.impl.FavoriteMovieServiceImpl;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@TestPropertySource(locations = "classpath:application.test.properties")
class FavoriteMovieServiceImplTest {

	@MockBean
	private FavoriteMovieRepository favoriteMovieRepository;

	@MockBean
	private MovieFeignClient movieFeignClient;

	@Autowired
	private FavoriteMovieServiceImpl favoriteMovieService;

	@BeforeEach
	void setUp() {

		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("user");
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	void createFavoriteMovie() {
		FavoriteMovieDTO favoriteMovieDTO = new FavoriteMovieDTO();
		favoriteMovieDTO.setUserId(1L);
		favoriteMovieDTO.setMovieId(1L);

		when(favoriteMovieRepository.existsByUserIdAndMovieId(anyLong(), anyLong())).thenReturn(false);
		when(movieFeignClient.existById(anyString(), anyLong())).thenReturn(ResponseEntity.ok(true));
		when(favoriteMovieRepository.save(any(FavoriteMovie.class))).thenAnswer(invocation -> {
			FavoriteMovie favoriteMovie = invocation.getArgument(0);
			favoriteMovie.setId(1L);
			return favoriteMovie;
		});

		FavoriteMovieDTO result = favoriteMovieService.createFavoriteMovie(favoriteMovieDTO);

		assertNotNull(result);
		assertEquals(1L, result.getUserId());
		assertEquals(1L, result.getMovieId());
	}

	@Test
	void createFavoriteMovieMovieAlreadyFavorited() {
		FavoriteMovieDTO favoriteMovieDTO = new FavoriteMovieDTO();
		favoriteMovieDTO.setUserId(1L);
		favoriteMovieDTO.setMovieId(1L);

		when(favoriteMovieRepository.existsByUserIdAndMovieId(anyLong(), anyLong())).thenReturn(true);

		Exception exception = assertThrows(RuntimeException.class, () -> {
			favoriteMovieService.createFavoriteMovie(favoriteMovieDTO);
		});

		assertEquals("Movie is already favorited", exception.getMessage());
	}

	@Test
	void createFavoriteMovieMovieNotFound() {
		FavoriteMovieDTO favoriteMovieDTO = new FavoriteMovieDTO();
		favoriteMovieDTO.setUserId(1L);
		favoriteMovieDTO.setMovieId(1L);

		when(favoriteMovieRepository.existsByUserIdAndMovieId(anyLong(), anyLong())).thenReturn(false);
		when(movieFeignClient.existById(anyString(), anyLong())).thenReturn(ResponseEntity.ok(false));

		Exception exception = assertThrows(RuntimeException.class, () -> {
			favoriteMovieService.createFavoriteMovie(favoriteMovieDTO);
		});

		assertEquals("Movie not found", exception.getMessage());
	}

	@Test
	void deleteFavoriteMovie() {
		FavoriteMovie favoriteMovie = new FavoriteMovie();
		favoriteMovie.setId(1L);
		favoriteMovie.setUserId(1L);
		favoriteMovie.setMovieId(1L);

		when(favoriteMovieRepository.findByUserIdAndMovieId(anyLong(), anyLong())).thenReturn(favoriteMovie);

		assertDoesNotThrow(() -> favoriteMovieService.deleteFavoriteMovie(1L, 1L));

		verify(favoriteMovieRepository, times(1)).deleteById(1L);
	}

	@Test
	void deleteFavoriteMovieNotFavorited() {
		when(favoriteMovieRepository.findByUserIdAndMovieId(anyLong(), anyLong())).thenReturn(null);

		Exception exception = assertThrows(RuntimeException.class, () -> {
			favoriteMovieService.deleteFavoriteMovie(1L, 1L);
		});

		assertEquals("this movie is not your favorited movie", exception.getMessage());
	}

	@Test
	void getAllFavoriteMovie() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Long> movieIdPage = new PageImpl<>(List.of(1L), pageable, 1);

		MovieDTO movieDTO = new MovieDTO();
		movieDTO.setId(1L);
		movieDTO.setName("Test Movie");

		when(favoriteMovieRepository.findAllByUserId(anyLong(), any(Pageable.class))).thenReturn(movieIdPage);
		when(movieFeignClient.getAllMoviesByIds(anyString(), anyList()))
				.thenReturn(ResponseEntity.ok(List.of(movieDTO)));

		Page<MovieDTO> result = favoriteMovieService.getAllFavoriteMovie(1L, pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals(1L, result.getContent().get(0).getId());
		assertEquals("Test Movie", result.getContent().get(0).getName());
	}
}

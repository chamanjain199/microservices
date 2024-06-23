package com.example.movie.rate.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import com.example.movie.rate.constant.Constant;
import com.example.movie.rate.dto.MovieRatingDTO;
import com.example.movie.rate.entity.MovieRating;
import com.example.movie.rate.exception.MovieNotFoundException;
import com.example.movie.rate.exception.MovieRatingNotFoundException;
import com.example.movie.rate.feignclient.MovieFeignClient;
import com.example.movie.rate.repository.MovieRatingRepository;
import com.example.movie.rate.service.impl.MovieRatingServiceImpl;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@TestPropertySource(locations = "classpath:application.test.properties")
public class MovieRatingServiceImplTest {

	@MockBean
	private MovieRatingRepository movieRatingRepository;

	@MockBean
	private MovieFeignClient movieFeignClient;

	@MockBean
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private MovieRatingServiceImpl movieRatingService;

	private MovieRatingDTO movieRatingDTO;

	private MovieRating movieRating;

	@BeforeEach
	public void setUp() {

		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("user");
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		LocalDateTime now = LocalDateTime.now();

		movieRatingDTO = new MovieRatingDTO();
		movieRatingDTO.setId(1L);
		movieRatingDTO.setMovieId(1L);
		movieRatingDTO.setRateBy("testUser");
		movieRatingDTO.setRating(4);
		movieRatingDTO.setRateDate(now);

		movieRating = new MovieRating();
		movieRating.setId(1L);
		movieRating.setMovieId(1L);
		movieRating.setRateBy("testUser");
		movieRating.setRating(4);
		movieRating.setRateDate(now);

	}

	@Test
	public void testCreateMovieRating_Success() {

		when(movieFeignClient.existById(anyString(), anyLong())).thenReturn(ResponseEntity.ok(true));
		when(movieRatingRepository.save(any(MovieRating.class))).thenAnswer(invocation -> {
			MovieRating movieRating = invocation.getArgument(0);
			movieRating.setId(1L);
			return movieRating;
		});

		when(movieRatingRepository.findRatingOfMovie(anyLong())).thenReturn(4.5);

		MovieRatingDTO result = movieRatingService.createMovieRating(movieRatingDTO);

		assertNotNull(result);
		verify(kafkaTemplate, times(1)).send(eq(Constant.KAFKA_TOPIC_RATE_MOVIE), anyString(), anyString());
	}

	@Test
	public void testCreateMovieRating_MovieNotExist() {

		when(movieFeignClient.existById(anyString(), anyLong())).thenReturn(ResponseEntity.ok(false));

		assertThrows(MovieNotFoundException.class, () -> movieRatingService.createMovieRating(movieRatingDTO));
	}

	@Test
	public void testDeleteMovieRating() {
		Long ratingId = 1L;

		when(movieRatingRepository.existsById(ratingId)).thenReturn(true);

		assertDoesNotThrow(() -> movieRatingService.deleteMovieRating(ratingId));
		verify(movieRatingRepository, times(1)).deleteById(ratingId);
	}

	@Test
	public void testDeleteMovieRatingNotFound() {
		Long ratingId = 1L;

		when(movieRatingRepository.existsById(ratingId)).thenReturn(false);

		assertThrows(MovieRatingNotFoundException.class, () -> movieRatingService.deleteMovieRating(ratingId));
	}

	@Test
	public void testGetMovieRatingById() {
		when(movieRatingRepository.findById(movieRating.getId())).thenReturn(Optional.of(movieRating));

		MovieRatingDTO result = movieRatingService.getMovieRatingById(movieRating.getId());

		assertNotNull(result);
		compareObject(result);
	}

	@Test
	public void testGetMovieRatingByIdNotFound() {
		Long ratingId = 1L;
		when(movieRatingRepository.findById(ratingId)).thenReturn(Optional.empty());
		assertThrows(MovieRatingNotFoundException.class, () -> movieRatingService.getMovieRatingById(ratingId));
	}

	@Test
	public void testGetAllMovieRatings() {
		Long movieId = 1L;
		Pageable pageable = Pageable.ofSize(10);

		Page<MovieRating> page = new PageImpl<>(List.of(movieRating));

		when(movieRatingRepository.findAllByMovieId(movieId, pageable)).thenReturn(page);

		Page<MovieRatingDTO> result = movieRatingService.getAllMovieRatings(movieId, pageable);

		assertNotNull(result);
		compareObject(result.getContent().get(0));
		assertEquals(1, result.getTotalElements());
	}

	private void compareObject(MovieRatingDTO movieRatingDTO) {
		assertEquals(movieRatingDTO.getId(), movieRating.getId());
		assertEquals(movieRatingDTO.getMovieId(), movieRating.getMovieId());
		assertEquals(movieRatingDTO.getRateBy(), movieRating.getRateBy());
		assertEquals(movieRatingDTO.getRateDate(), movieRating.getRateDate());
		assertEquals(movieRatingDTO.getRating(), movieRating.getRating());
	}

}
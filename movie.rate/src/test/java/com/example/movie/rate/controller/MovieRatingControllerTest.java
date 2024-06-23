package com.example.movie.rate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movie.rate.dto.MovieRatingDTO;
import com.example.movie.rate.service.MovieRatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@TestPropertySource(locations = "classpath:application.test.properties")
@AutoConfigureMockMvc
public class MovieRatingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MovieRatingService movieRatingService;

	@MockBean
	private KafkaTemplate<String, String> kafkaTemplate;

	MovieRatingDTO movieRatingDTO;

	@BeforeEach
	public void setUp() {
		movieRatingDTO =new MovieRatingDTO();
		movieRatingDTO.setId(1L);
		movieRatingDTO.setMovieId(1L);
		movieRatingDTO.setRateBy("testUser");
		movieRatingDTO.setRateDate(LocalDateTime.now());
		movieRatingDTO.setRating(3);
	}

	@Test
	public void testCreateMovieRating_Success() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		when(movieRatingService.createMovieRating(any(MovieRatingDTO.class))).thenReturn(movieRatingDTO);

		mockMvc.perform(post("/movie-rating").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")
				.content(objectMapper.writeValueAsString(movieRatingDTO))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.movieId").value(1L)).andExpect(jsonPath("$.rating").value(3))
				.andExpect(jsonPath("$.rateBy").value("testUser")).andExpect(jsonPath("$.id").value(1));

	}

	@Test
	public void testDeleteMovieRating_Success() throws Exception {
		mockMvc.perform(delete("/movie-rating/{id}", 1L).header("X-Username", "testUser"))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testGetMovieRatingById_Success() throws Exception {

		when(movieRatingService.getMovieRatingById(anyLong())).thenReturn(movieRatingDTO);

		mockMvc.perform(get("/movie-rating/{id}", 1L).header("X-Username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.movieId").value(1L))
				.andExpect(jsonPath("$.rating").value(3));
	}

	@Test
	public void testGetAllMovieRatings_Success() throws Exception {

		Page<MovieRatingDTO> page = new PageImpl<>(List.of(movieRatingDTO));

		when(movieRatingService.getAllMovieRatings(anyLong(), any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/movie-rating").param("movieId", "1").param("page", "0").param("size", "10")
				.header("X-Username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(1L)).andExpect(jsonPath("$.content[0].movieId").value(1L))
				.andExpect(jsonPath("$.content[0].rating").value(3));
	}
}

package com.example.movie.fav.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movie.fav.dto.FavoriteMovieDTO;
import com.example.movie.fav.dto.MovieDTO;
import com.example.movie.fav.service.FavoriteMovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@TestPropertySource(locations = "classpath:application.test.properties")
@AutoConfigureMockMvc
class FavoriteMovieControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FavoriteMovieService favoriteMovieService;

	FavoriteMovieDTO favoriteMovieDTO;

	@BeforeEach
	void setUp() {
		favoriteMovieDTO = new FavoriteMovieDTO();
		favoriteMovieDTO.setUserId(1L);
		favoriteMovieDTO.setMovieId(1L);
		favoriteMovieDTO.setCreatedDate(LocalDateTime.now());
	}

	@Test
	void favoriteAMovie() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		when(favoriteMovieService.createFavoriteMovie(any(FavoriteMovieDTO.class))).thenReturn(favoriteMovieDTO);
		mockMvc.perform(post("/api/movie-fav").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")
				.content(objectMapper.writeValueAsString(favoriteMovieDTO))).andExpect(status().isAccepted())
				.andExpect(jsonPath("$.userId").value(1L)).andExpect(jsonPath("$.movieId").value(1L));
	}

	@Test
	void unfavoriteAMovie_success() throws Exception {
		mockMvc.perform(delete("/api/movie-fav").param("userId", "1").param("movieId", "1").header("X-Username", "testUser"))
				.andExpect(status().isNoContent());
	}

	@Test
	void getAllFavoriteMovieOfUser_success() throws Exception {
		MovieDTO movieDTO = new MovieDTO();
		movieDTO.setId(1L);
		movieDTO.setName("Test Movie");
		Pageable pageable = PageRequest.of(0, 10);
		Page<MovieDTO> page = new PageImpl<>(Collections.singletonList(movieDTO), pageable, 1);
		when(favoriteMovieService.getAllFavoriteMovie(anyLong(), any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/api/movie-fav").param("userId", "1").param("page", "0").param("size", "10").header("X-Username", "testUser"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id").value(1L))
				.andExpect(jsonPath("$.content[0].name").value("Test Movie"));
	}
}

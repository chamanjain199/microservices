package com.example.movie.rate.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movie.rate.dto.MovieCommentDTO;
import com.example.movie.rate.service.MovieCommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@TestPropertySource(locations = "classpath:application.test.properties")
@AutoConfigureMockMvc
public class MovieCommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MovieCommentService movieCommentService;
	
	@MockBean
	private KafkaTemplate<String, String> kafkaTemplate;

	private MovieCommentDTO movieCommentDTO;

	@BeforeEach
	public void setUp() {
		movieCommentDTO = new MovieCommentDTO();
		movieCommentDTO.setComment("Great movie!");
		movieCommentDTO.setCommnendBy("testUser");
		movieCommentDTO.setId(1L);
		movieCommentDTO.setMovieId(1L);
	}


	@Test
	public void testCreateMovieComment() throws Exception {
		ObjectMapper objectMapper=new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		when(movieCommentService.createMovieComment(any())).thenReturn(movieCommentDTO);
		mockMvc.perform(post("/movie-comment").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")
				.content(objectMapper.writeValueAsString(movieCommentDTO))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.movieId").value(1L)).andExpect(jsonPath("$.comment").value("Great movie!"))
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.commnendBy").value("testUser"));
	}

	@Test
	public void testDeleteMovieComment() throws Exception {
		mockMvc.perform(delete("/movie-comment/{id}", 1L).header("X-Username", "testUser"))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testGetMovieCommentById() throws Exception {

		when(movieCommentService.getMovieCommentById(anyLong())).thenReturn(movieCommentDTO);

		mockMvc.perform(get("/movie-comment/{id}", 1L).header("X-Username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$.movieId").value(1L)).andExpect(jsonPath("$.comment").value("Great movie!"))
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.commnendBy").value("testUser"));
	}

	@Test
	public void testGetAllMovieComments() throws Exception {

		Page<MovieCommentDTO> page = new PageImpl<>(List.of(movieCommentDTO));
		when(movieCommentService.getAllMovieComments(anyLong(), any())).thenReturn(page);

		mockMvc.perform(get("/movie-comment").param("movieId", "1").param("page", "0").param("size", "10")
				.header("X-Username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(1L)).andExpect(jsonPath("$.content[0].movieId").value(1L))
				.andExpect(jsonPath("$.content[0].comment").value("Great movie!"));
	}
}
package com.example.movie.repository.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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

import com.example.movie.repository.dto.MovieDTO;
//import com.example.movie.repository.elasticsearch.repository.MovieDocumentRepository;
import com.example.movie.repository.exception.MovieNotFoundException;
import com.example.movie.repository.repository.MovieRepository;
import com.example.movie.repository.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@TestPropertySource(locations = "classpath:application.test.properties")
@AutoConfigureMockMvc
public class MovieControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MovieService movieService;

	@MockBean
	private MovieRepository movieRepository;

//	@MockBean
//	private MovieDocumentRepository movieDocumentRepository;

	private MovieDTO movieDTO;
	private Page<MovieDTO> moviePage;

	@BeforeEach
	public void setup() {
		movieDTO = new MovieDTO();
		movieDTO.setId(1L);
		movieDTO.setRating(3);
		movieDTO.setDescription("Desc");
		movieDTO.setGenre("Drama");
		movieDTO.setLanguage("Hindi");
		movieDTO.setName("ABC");
		movieDTO.setLengthInMin(120);
		movieDTO.setReleaseYear(2020);

		List<MovieDTO> movieDTOs = Arrays.asList(movieDTO);
		moviePage = new PageImpl<>(movieDTOs, PageRequest.of(0, 10), 1);
	}

	@Test
	public void testCreateMovie() throws Exception {
		when(movieService.createMovie(any(MovieDTO.class))).thenReturn(movieDTO);

		mockMvc.perform(post("/api/movie").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")
				.content(new ObjectMapper().writeValueAsString(movieDTO))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(movieDTO.getId()))
				.andExpect(jsonPath("$.description").value(movieDTO.getDescription()))
				.andExpect(jsonPath("$.rating").value(movieDTO.getRating()))
				.andExpect(jsonPath("$.genre").value(movieDTO.getGenre()))
				.andExpect(jsonPath("$.lengthInMin").value(movieDTO.getLengthInMin()))
				.andExpect(jsonPath("$.releaseYear").value(movieDTO.getReleaseYear()))
				.andExpect(jsonPath("$.name").value(movieDTO.getName()));

	}

	@Test
	public void testGetMovieById() throws Exception {
		when(movieService.getMovieById(anyLong())).thenReturn(movieDTO);

		mockMvc.perform(get("/api/movie/{id}", 1L).header("X-Username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(movieDTO.getId()))
				.andExpect(jsonPath("$.description").value(movieDTO.getDescription()))
				.andExpect(jsonPath("$.rating").value(movieDTO.getRating()))
				.andExpect(jsonPath("$.genre").value(movieDTO.getGenre()))
				.andExpect(jsonPath("$.lengthInMin").value(movieDTO.getLengthInMin()))
				.andExpect(jsonPath("$.releaseYear").value(movieDTO.getReleaseYear()))
				.andExpect(jsonPath("$.name").value(movieDTO.getName()));
	}

	@Test
	public void testGetMovieById_NotFound() throws Exception {
		when(movieService.getMovieById(anyLong())).thenThrow(new MovieNotFoundException("Movie not found"));

		mockMvc.perform(get("/api/movie/{id}", 1L).header("X-Username", "testUser")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetAllMovies() throws Exception {
		when(movieService.getAllMovies(any(Pageable.class))).thenReturn(moviePage);

		mockMvc.perform(get("/api/movie").param("page", "0").param("size", "10").header("X-Username", "testUser"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id").value(movieDTO.getId()))
				.andExpect(jsonPath("$.content[0].description").value(movieDTO.getDescription()))
				.andExpect(jsonPath("$.content[0].rating").value(movieDTO.getRating()))
				.andExpect(jsonPath("$.content[0].genre").value(movieDTO.getGenre()))
				.andExpect(jsonPath("$.content[0].lengthInMin").value(movieDTO.getLengthInMin()))
				.andExpect(jsonPath("$.content[0].releaseYear").value(movieDTO.getReleaseYear()))
				.andExpect(jsonPath("$.content[0].name").value(movieDTO.getName()));
	}

	@Test
	public void testDeleteMovie() throws Exception {
		mockMvc.perform(delete("/api/movie/{id}", 1L).header("X-Username", "testUser"))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteMovie_NotFound() throws Exception {
		doThrow(new MovieNotFoundException("Movie not found")).when(movieService).deleteMovie(anyLong());

		mockMvc.perform(delete("/api/movie/{id}", 1L).header("X-Username", "testUser"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testExistById() throws Exception {
		when(movieService.existById(anyLong())).thenReturn(true);

		mockMvc.perform(get("/api/movie/exist/{id}", 1L).header("X-Username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$").value(true));
	}

	@Test
	public void testGetAllMoviesByIds() throws Exception {
		List<MovieDTO> movieDTOs = Arrays.asList(movieDTO);
		when(movieService.getAllMoviesByIds(anyList())).thenReturn(movieDTOs);

		mockMvc.perform(get("/api/movie/byids").param("ids", "1,2,3").header("X-Username", "testUser"))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(movieDTO.getId()))
				.andExpect(jsonPath("$[0].description").value(movieDTO.getDescription()))
				.andExpect(jsonPath("$[0].rating").value(movieDTO.getRating()))
				.andExpect(jsonPath("$[0].genre").value(movieDTO.getGenre()))
				.andExpect(jsonPath("$[0].lengthInMin").value(movieDTO.getLengthInMin()))
				.andExpect(jsonPath("$[0].releaseYear").value(movieDTO.getReleaseYear()))
				.andExpect(jsonPath("$[0].name").value(movieDTO.getName()));
	}

}

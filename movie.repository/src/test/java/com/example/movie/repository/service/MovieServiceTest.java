package com.example.movie.repository.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import com.example.movie.repository.dto.MovieDTO;
//import com.example.movie.repository.elasticsearch.repository.MovieDocumentRepository;
import com.example.movie.repository.entity.Movie;
import com.example.movie.repository.repository.MovieRepository;
import com.example.movie.repository.service.impl.MovieServiceImpl;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@TestPropertySource(locations = "classpath:application.test.properties")
public class MovieServiceTest {

	@MockBean
	private MovieRepository movieRepository;

//	@MockBean
//	private MovieDocumentRepository movieDocumentRepository;

	@Autowired
	private MovieServiceImpl movieService;

	private MovieDTO movieDTO;

	private Movie movie;

	@BeforeEach
	void setUp() {
		movieDTO = new MovieDTO();
		movieDTO.setRating(3);
		movieDTO.setDescription("Desc");
		movieDTO.setGenre("Drama");
		movieDTO.setLanguage("Hindi");
		movieDTO.setName("ABC");
		movieDTO.setLengthInMin(120);
		movieDTO.setReleaseYear(2020);

		movie = new Movie();
		movie.setId(1L);
		movie.setRating(3);
		movie.setDescription("Desc");
		movie.setGenre("Drama");
		movie.setLanguage("Hindi");
		movie.setName("ABC");
		movie.setLengthInMin(120);
		movie.setReleaseYear(2020);
	}

	@Test
	public void createMovieTest() {
		when(movieRepository.save(Mockito.any())).thenReturn(movie);
		MovieDTO newMovieDto = movieService.createMovie(movieDTO);
		compareMovieObject(newMovieDto);
	}

	@Test
	public void getMovieByIdTest() {
		when(movieRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(movie));
		MovieDTO newMovieDto = movieService.getMovieById(1L);
		compareMovieObject(newMovieDto);
	}

	@Test
	public void getMovieByIdTestNotFound() {
		when(movieRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			movieService.getMovieById(1L);
		});

		assertEquals("Movie not found", exception.getMessage());
	}

	@Test
	public void getAllMoviesTest() {
		Pageable pageable = PageRequest.of(0, 10);
		List<Movie> movies = Arrays.asList(movie);
		Page<Movie> moviePage = new PageImpl<>(movies, pageable, movies.size());

		when(movieRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(moviePage);

		Page<MovieDTO> result = movieService.getAllMovies(pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		compareMovieObject(result.getContent().get(0));

	}

	@Test
	public void testDeleteMovie() {
		when(movieRepository.existsById(anyLong())).thenReturn(true);
//		doNothing().when(movieDocumentRepository).deleteById(anyLong());
		doNothing().when(movieRepository).deleteById(anyLong());

		movieService.deleteMovie(1L);

		verify(movieRepository, times(1)).existsById(1L);
//		verify(movieDocumentRepository, times(1)).deleteById(1L);
		verify(movieRepository, times(1)).deleteById(1L);
	}

	@Test
	public void testDeleteMovieNotFound() {
		when(movieRepository.existsById(anyLong())).thenReturn(false);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			movieService.deleteMovie(1L);
		});

		assertEquals("Movie not found", exception.getMessage());
	}

	@Test
	public void testExistById() {
		when(movieRepository.existsById(anyLong())).thenReturn(true);

		boolean exists = movieService.existById(1L);

		assertThat(exists).isTrue();
		verify(movieRepository, times(1)).existsById(1L);
	}

	@Test
	public void testGetAllMoviesByIds() {
		List<Long> ids = Arrays.asList(1L, 2L);
		List<Movie> movies = Arrays.asList(movie);
		when(movieRepository.findAllById(ids)).thenReturn(movies);

		List<MovieDTO> result = movieService.getAllMoviesByIds(ids);

		assertNotNull(result);
		assertEquals(1, result.size());
		compareMovieObject(result.get(0));

		verify(movieRepository, times(1)).findAllById(ids);
	}

	private void compareMovieObject(MovieDTO newMovieDto) {
		assertEquals(newMovieDto.getDescription(), movieDTO.getDescription());
		assertEquals(newMovieDto.getGenre(), movieDTO.getGenre());
		assertEquals(newMovieDto.getRating(), movieDTO.getRating());
		assertEquals(newMovieDto.getLanguage(), movieDTO.getLanguage());
		assertEquals(newMovieDto.getName(), movieDTO.getName());
		assertEquals(newMovieDto.getLengthInMin(), movieDTO.getLengthInMin());
		assertEquals(newMovieDto.getReleaseYear(), movieDTO.getReleaseYear());
	}

}

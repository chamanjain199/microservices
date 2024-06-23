package com.example.movie.rate.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.example.movie.rate.dto.MovieCommentDTO;
import com.example.movie.rate.entity.MovieComment;
import com.example.movie.rate.exception.MovieCommnetNotFoundException;
import com.example.movie.rate.exception.MovieNotFoundException;
import com.example.movie.rate.feignclient.MovieFeignClient;
import com.example.movie.rate.repository.MovieCommentRepository;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@TestPropertySource(locations = "classpath:application.test.properties")
public class MovieCommentServiceTest {

	@MockBean
	private MovieCommentRepository movieCommentRepository;

	@MockBean
	private MovieFeignClient movieFeignClient;

	@MockBean
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private MovieCommentService movieCommentService;

	private MovieCommentDTO movieCommentDTO;
	private MovieComment movieComment;

	@BeforeEach
	public void setup() {

		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("user");
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		LocalDateTime now = LocalDateTime.now();

		movieCommentDTO = new MovieCommentDTO();
		movieCommentDTO.setComment("Comment");
		movieCommentDTO.setCommentDate(now);
		movieCommentDTO.setCommnendBy("testUser");
		movieCommentDTO.setId(1L);
		movieCommentDTO.setMovieId(2L);

		movieComment = new MovieComment();
		movieComment.setId(1L);
		movieComment.setComment("Comment");
		movieComment.setCommentDate(now);
		movieComment.setCommnendBy("testUser");
		movieComment.setId(1L);
		movieComment.setMovieId(2L);

	}

	@Test
	public void testCreateMovieComment() throws Exception {
		when(movieFeignClient.existById(anyString(), any())).thenReturn(ResponseEntity.ok(true));
		when(movieCommentRepository.save(any())).thenAnswer(invocation -> {
			MovieComment movieComment=invocation.getArgument(0);
			movieComment.setId(1L);
			return movieComment;
		});
		MovieCommentDTO createdComment = movieCommentService.createMovieComment(movieCommentDTO);
		compareObject(createdComment);
		verify(movieFeignClient, times(1)).existById(anyString(), any());
		verify(movieCommentRepository, times(1)).save(any(MovieComment.class));
	}

	@Test
	public void testCreateMovieCommentMovieNotExist() {
		when(movieFeignClient.existById(anyString(), anyLong())).thenReturn(ResponseEntity.ok(false));
		assertThrows(MovieNotFoundException.class, () -> movieCommentService.createMovieComment(movieCommentDTO));
	}

	@Test
	public void testDeleteMovieComment() {
		Long commentId = 1L;
		when(movieCommentRepository.existsById(commentId)).thenReturn(true);
		assertDoesNotThrow(() -> movieCommentService.deleteMovieComment(commentId));
		verify(movieCommentRepository, times(1)).deleteById(commentId);
	}

	@Test
	public void testDeleteMovieCommentNotFound() {
		Long commentId = 1L;
		when(movieCommentRepository.existsById(commentId)).thenReturn(false);
		assertThrows(MovieCommnetNotFoundException.class, () -> movieCommentService.deleteMovieComment(commentId));
	}

	@Test
	public void testGetMovieCommentById() {
		Long commentId = 1L;
		when(movieCommentRepository.findById(commentId)).thenReturn(Optional.of(movieComment));
		MovieCommentDTO result = movieCommentService.getMovieCommentById(commentId);
		assertNotNull(result);
		compareObject(result);

	}

	@Test
	public void testGetMovieCommentByIdNotFound() {
		Long commentId = 1L;
		when(movieCommentRepository.findById(commentId)).thenReturn(Optional.empty());
		assertThrows(MovieCommnetNotFoundException.class, () -> movieCommentService.getMovieCommentById(commentId));
	}

	@Test
	public void testGetAllMovieComments() {
		Long movieId = 1L;
		Pageable pageable = Pageable.ofSize(10);

		Page<MovieComment> page = new PageImpl<>(List.of(movieComment));

		when(movieCommentRepository.findAllByMovieId(movieId, pageable)).thenReturn(page);

		Page<MovieCommentDTO> result = movieCommentService.getAllMovieComments(movieId, pageable);
		assertNotNull(result);
		compareObject(result.getContent().get(0));
	}

	private void compareObject(MovieCommentDTO movieCommentDTO) {
		assertEquals(movieCommentDTO.getId(), movieComment.getId());
		assertEquals(movieCommentDTO.getComment(), movieComment.getComment());
		assertEquals(movieCommentDTO.getCommentDate(), movieComment.getCommentDate());
		assertEquals(movieCommentDTO.getMovieId(), movieComment.getMovieId());
		assertEquals(movieCommentDTO.getCommnendBy(), movieComment.getCommnendBy());
	}

}
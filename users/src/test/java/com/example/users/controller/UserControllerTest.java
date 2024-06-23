package com.example.users.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.users.dto.UsersDTO;
import com.example.users.exception.UserNotFoundException;
import com.example.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest()
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private UsersDTO userDTO;

	@BeforeEach
	public void setUp() {
		userDTO = new UsersDTO();
		userDTO.setId(1);
		userDTO.setName("test");
		userDTO.setEmail("test@example.com");
		userDTO.setPassword("password");
	}

	@Test
	public void testCreateUser() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		when(userService.saveUser(any(UsersDTO.class))).thenReturn(userDTO);

		mockMvc.perform(post("/api/user/register").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")
				.content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("test")).andExpect(jsonPath("$.email").value("test@example.com"));
	}

	@Test
	public void testGetUserByIdUserExists() throws Exception {
		when(userService.getUserById(1)).thenReturn(userDTO);

		mockMvc.perform(get("/api/user/1").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("test")).andExpect(jsonPath("$.email").value("test@example.com"));
	}

	@Test
	public void testGetUserByIdUserDoesNotExist() throws Exception {
		when(userService.getUserById(1)).thenReturn(null);

		mockMvc.perform(get("/api/user/1").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetAllUsers() throws Exception {
		List<UsersDTO> usersDTOList = Arrays.asList(userDTO);
		when(userService.getAllUsers()).thenReturn(usersDTOList);

		mockMvc.perform(get("/api/user").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].name").value("test")).andExpect(jsonPath("$[0].email").value("test@example.com"));
	}

	@Test
	public void testDeleteUser() throws Exception {
		doNothing().when(userService).deleteUser(1);

		mockMvc.perform(delete("/api/user/1").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser"))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteUserUserDoesNotExist() throws Exception {
		doThrow(new UserNotFoundException("User not found.")).when(userService).deleteUser(1);

		mockMvc.perform(delete("/api/user/1").contentType(MediaType.APPLICATION_JSON).header("X-Username", "testUser")).andExpect(status().isNotFound());
	}
}
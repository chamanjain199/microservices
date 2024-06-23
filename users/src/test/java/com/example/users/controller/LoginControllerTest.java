package com.example.users.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.users.service.impl.UserDetailServiceImpl;

@SpringBootTest()
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDetailServiceImpl UserDetailService;

	@Test
	public void testLogin() throws Exception {
		when(UserDetailService.loadUserByUsername(any()))
				.thenReturn(new User("username", "$2a$12$mQ8B0OE9iPZ/tB2TIsn5w.gfl0nkOT/EZl8KO5rd1CcBiQSSAxFyW", new ArrayList<>()));

		String username = "username";
		String password = "password";

		String basicAuthHeader = "Basic "
				+ java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

		mockMvc.perform(
				get("/api/login").header("Authorization", basicAuthHeader).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(header().exists("Authorization"));
	}

}
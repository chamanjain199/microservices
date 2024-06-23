package com.example.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import com.example.users.entity.Users;
import com.example.users.repository.UserRepository;
import com.example.users.service.impl.UserDetailServiceImpl;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserDetailServiceImplTest {

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserDetailServiceImpl userDetailService;

	@Test
	public void testLoadUserByUsername() {
		String username = "test@example.com";
		String password = "$2a$12$mQ8B0OE9iPZ/tB2TIsn5w.gfl0nkOT/EZl8KO5rd1CcBiQSSAxFyW";
		Users mockUser = new Users();
		mockUser.setEmail(username);
		mockUser.setPassword(password);

		when(userRepository.findByEmail(username)).thenReturn(mockUser);

		UserDetails userDetails = userDetailService.loadUserByUsername(username);
		assertNotNull(userDetails);
		assertEquals(username, userDetails.getUsername());
		assertEquals(password, userDetails.getPassword());
		assertTrue(userDetails.getAuthorities().isEmpty());
	}

	@Test
	public void testLoadUserByUsername_InvalidUsername() {
		String invalidUsername = "invalid@example.com";
		when(userRepository.findByEmail(invalidUsername)).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> {
			userDetailService.loadUserByUsername(invalidUsername);
		});
	}
}

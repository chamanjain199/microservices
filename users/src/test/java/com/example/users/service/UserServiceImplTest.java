package com.example.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.example.users.dto.UsersDTO;
import com.example.users.entity.Users;
import com.example.users.exception.UserAlreadyExistException;
import com.example.users.repository.UserRepository;
import com.example.users.service.impl.UserServiceImpl;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceImplTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserServiceImpl userService;

	private Users user;
	private UsersDTO userDTO;

	@BeforeEach
	public void setUp() {
		user = new Users();
		user.setId(1);
		user.setEmail("test@example.com");
		user.setPassword("password");

		userDTO = new UsersDTO();
		userDTO.setId(1);
		userDTO.setEmail("test@example.com");
		userDTO.setPassword("password");
	}

	@Test
	public void testSaveUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(userRepository.save(any(Users.class))).thenReturn(user);

		UsersDTO savedUser = userService.saveUser(userDTO);

		assertNotNull(savedUser);
		assertEquals(userDTO.getEmail(), savedUser.getEmail());
		verify(userRepository, times(1)).save(any(Users.class));
	}

	@Test
	public void testSaveUserExistingUserThrowsException() {
		UsersDTO userDTO = new UsersDTO();
		userDTO.setEmail("test@example.com");
		userDTO.setPassword("password");
		when(userRepository.findByEmail(anyString())).thenReturn(user);

		assertThrows(UserAlreadyExistException.class, () -> userService.saveUser(userDTO));
		verify(userRepository, never()).save(any(Users.class));
	}

	@Test
	public void testGetUserByIdUserExists() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

		UsersDTO foundUser = userService.getUserById(1);

		assertNotNull(foundUser);
		assertEquals(userDTO.getEmail(), foundUser.getEmail());
	}

	@Test
	public void testGetUserByIDUserDoesNotExist() {
		when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> userService.getUserById(1));
	}

	@Test
	public void testGetAllUsers() {
		List<Users> usersList = Arrays.asList(user);
		when(userRepository.findAll()).thenReturn(usersList);

		List<UsersDTO> usersDTOList = userService.getAllUsers();

		assertNotNull(usersDTOList);
		assertEquals(1, usersDTOList.size());
	}

	@Test
	public void testDeleteUser_UserExists() {
		when(userRepository.existsById(anyInt())).thenReturn(true);

		userService.deleteUser(1);

		verify(userRepository, times(1)).deleteById(anyInt());
	}

	@Test
	public void testDeleteUser_UserDoesNotExist() {
		when(userRepository.existsById(anyInt())).thenReturn(false);

		assertThrows(RuntimeException.class, () -> userService.deleteUser(1));
		verify(userRepository, never()).deleteById(anyInt());
	}
}
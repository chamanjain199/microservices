package com.example.users.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.users.dto.UsersDTO;
import com.example.users.entity.Users;
import com.example.users.exception.UserAlreadyExistException;
import com.example.users.exception.UserNotFoundException;
import com.example.users.mapper.UsersMapper;
import com.example.users.repository.UserRepository;
import com.example.users.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UsersDTO saveUser(UsersDTO user) {
		Users newUser = UsersMapper.toEntity(user);
		if (newUser.getId() <= 0) {
			if(userRepository.findByEmail(user.getEmail())!=null) {
				throw new UserAlreadyExistException("Username already exists.");
			}
			newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return UsersMapper.toDto(userRepository.save(newUser));
	}

	@Override
	public UsersDTO getUserById(int id) {
		Optional<Users> userOp = userRepository.findById(id);
		if (userOp.isPresent()) {
			return UsersMapper.toDto(userOp.get());
		}
		throw new UserNotFoundException("User not found.");
	}

	@Override
	public List<UsersDTO> getAllUsers() {
		return UsersMapper.toDtoList(userRepository.findAll());
	}

	@Override
	public void deleteUser(int id) {
		if (!userRepository.existsById(id)) {
			throw new UserNotFoundException("User not found.");
		}
		userRepository.deleteById(id);
	}
}
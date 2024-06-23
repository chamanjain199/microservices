package com.example.users.service;

import java.util.List;

import com.example.users.dto.UsersDTO;

public interface UserService {

	UsersDTO saveUser(UsersDTO user);
	
	UsersDTO getUserById(int id);

	List<UsersDTO> getAllUsers();

	void deleteUser(int id);
}
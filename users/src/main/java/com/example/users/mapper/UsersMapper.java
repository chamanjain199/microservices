package com.example.users.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.example.users.dto.UsersDTO;
import com.example.users.entity.Users;

public class UsersMapper {

	public static UsersDTO toDto(Users user) {
		UsersDTO dto = new UsersDTO();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		return dto;
	}

	public static Users toEntity(UsersDTO dto) {
		Users user = new Users();
		user.setId(dto.getId());
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		return user;
	}

	public static List<UsersDTO> toDtoList(Collection<Users> users) {
		return users.stream().map(UsersMapper::toDto).collect(Collectors.toList());
	}

	public static List<Users> toEntityList(Collection<UsersDTO> dtos) {
		return dtos.stream().map(UsersMapper::toEntity).collect(Collectors.toList());
	}
}
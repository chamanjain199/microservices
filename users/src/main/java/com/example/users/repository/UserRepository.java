package com.example.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.users.entity.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
	Users findByEmail(String email);
}
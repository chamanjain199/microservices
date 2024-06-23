package com.example.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.users.repository.UserRepository;

@RestController
@RequestMapping("/api/login")
public class LoginController {
	@Autowired
	UserRepository userRepository;

	@GetMapping
	public ResponseEntity<Void> login() {
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

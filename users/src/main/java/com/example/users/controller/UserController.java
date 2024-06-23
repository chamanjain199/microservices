package com.example.users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.users.dto.UsersDTO;
import com.example.users.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Create a new user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UsersDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PostMapping("/register")
	public ResponseEntity<UsersDTO> createUser( @RequestBody @Valid UsersDTO user) {
		UsersDTO savedUser = userService.saveUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	@Operation(summary = "Get a user by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UsersDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<UsersDTO> getUserById(@PathVariable @NotNull Integer id) {
		UsersDTO user = userService.getUserById(id);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@Operation(summary = "Get all users")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Found all users", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsersDTO.class))) }) })
	@GetMapping
	public ResponseEntity<List<UsersDTO>> getAllUsers() {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}

	@Operation(summary = "Delete a user by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "User deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable @NotNull Integer id) {
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}

package com.example.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsersDTO {
	private int id;

	@NotBlank(message = "Name is required")
	private String name;

	@Email(message = "Please enter a valid email")
	@NotBlank(message = "Email is required")
	private String email;

	@Size(min = 3, message = "Min password lenght shoud be > 3")
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
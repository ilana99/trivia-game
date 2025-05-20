package com.fsp.trivia.authentication;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


public class RegisterDTO {
	
	@NotBlank(message = "username must not be empty")
	private String username;
	@NotBlank(message = "password must not be empty")
	private String password;
	@NotBlank(message = "email must not be empty")
	private String email;
	private String role;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	

}

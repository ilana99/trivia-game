package com.fsp.trivia.user;

import java.util.List;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Users", uniqueConstraints = {@UniqueConstraint (name="emailConstraint", columnNames="email") })
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Nonnull
	private String username;
	
	@Nonnull
	private String password;
	
	@Nonnull
	@NotBlank
	private String email;
	
	@Nonnull
	private String role;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Games> games; 
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public List<Games> getGames() {
		return games;
	}
	public void setGames(List<Games> games) {
		this.games = games;
	}

}


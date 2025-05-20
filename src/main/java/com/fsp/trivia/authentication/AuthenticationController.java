package com.fsp.trivia.authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsp.trivia.errors.ResourceNotFound;
import com.fsp.trivia.questions.ApiResponseDTO;
import com.fsp.trivia.user.UserRepository;
import com.fsp.trivia.user.Users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private JWTService jwtService;
	
	

	public AuthenticationController(UserRepository userRepository, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder, JWTService jwtService) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}



	@PostMapping("/login")
	public ResponseEntity<ApiResponseDTO<String>> loginUser(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {

			 authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
			 
			
			String jwt = jwtService.generateJWT(loginDTO.getUsername());
			if (jwt == null) {
				throw new ResourceNotFound("jwt not found");
			}
			Cookie cookie = new Cookie("jwt", jwt);
			cookie.setHttpOnly(true);
			cookie.setPath("/trivia");
			cookie.setSecure(false);
			response.addCookie(cookie);
			
			return ResponseEntity.ok(new ApiResponseDTO<>("Logged in"));			
			
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponseDTO<String>> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
			Users newUser = new Users();
			newUser.setUsername(registerDTO.getUsername());
			newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword())); 
			newUser.setEmail(registerDTO.getEmail());
			newUser.setRole("user"); 
			userRepository.save(newUser);
			return ResponseEntity.ok(new ApiResponseDTO<>("Registered user successfully"));			
	}

}

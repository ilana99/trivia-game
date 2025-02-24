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
import com.fsp.trivia.user.UserRepository;
import com.fsp.trivia.user.Users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

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
	public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		Map<String, String> responseBody = new HashMap<>();
		try {
			 authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
//			SecurityContextHolder.getContext().setAuthentication(authentication);
	
			
			String jwt = jwtService.generateJWT(loginDTO.getUsername());
			Cookie cookie = new Cookie("jwt", jwt);
			cookie.setHttpOnly(true);
			cookie.setPath("/trivia");
			cookie.setSecure(false);
			response.addCookie(cookie);
			
//			responseBody.put("message", "login successful");
						
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			e.printStackTrace();
			responseBody.put("message", "invalid credentials");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
		}
	}

	@PostMapping("/register")
	public String registerUser(@RequestBody RegisterDTO registerDTO) {
		try {
			Users newUser = new Users();
			newUser.setUsername(registerDTO.getUsername());
			newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword())); 
			newUser.setEmail(registerDTO.getEmail());
			newUser.setRole("user"); 
			userRepository.save(newUser);
			return "user ok";
		} catch (Exception e) {
			e.printStackTrace();
			return "user not ok";
		}
	}

}

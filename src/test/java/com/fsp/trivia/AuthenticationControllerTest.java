package com.fsp.trivia;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fsp.trivia.authentication.AuthenticationController;
import com.fsp.trivia.authentication.JWTService;
import com.fsp.trivia.authentication.LoginDTO;
import com.fsp.trivia.authentication.RegisterDTO;
import com.fsp.trivia.errors.ResourceNotFound;
import com.fsp.trivia.questions.ApiResponseDTO;
import com.fsp.trivia.user.UserRepository;
import com.fsp.trivia.user.Users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {


	@InjectMocks
	AuthenticationController authenticationController;
	@Mock
	private UserRepository userRepository;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private JWTService jwtService;
	@Mock
	HttpServletResponse response;
	@Mock
	private PasswordEncoder passwordEncoder;

	private LoginDTO loginDTO;
	private String jwt;
	private Authentication authentication;
	private RegisterDTO registerDTO;
	private Users mockUser;
	
	@BeforeEach
	public void setUp() {
		loginDTO = new LoginDTO();
		loginDTO.setUsername("username");
		loginDTO.setPassword("test");
		jwt = "test";

		registerDTO = new RegisterDTO();
		registerDTO.setUsername("username");
		registerDTO.setPassword("password");
		registerDTO.setEmail("email");
		
		mockUser = new Users();
		mockUser.setUsername(registerDTO.getUsername());
		mockUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		mockUser.setEmail(registerDTO.getEmail());
		mockUser.setRole("user");
		
		
	}

	@Test
	public void whenLogin_thenReturnOk() throws Exception {

		when(authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())))
				.thenReturn(authentication);

		when(jwtService.generateJWT(loginDTO.getUsername())).thenReturn(jwt);

		ResponseEntity<ApiResponseDTO<String>> responseLogin = authenticationController.loginUser(loginDTO, response);
		
		assertEquals(HttpStatus.OK, responseLogin.getStatusCode());

	}

	@Test
	public void givenCookieJWT_whenLogin_thenReturnOk() throws Exception {
		when(jwtService.generateJWT(loginDTO.getUsername())).thenReturn(jwt);

		ArgumentCaptor<Cookie> captor = ArgumentCaptor.forClass(Cookie.class);

		ResponseEntity<ApiResponseDTO<String>> responseLogin = authenticationController.loginUser(loginDTO, response);

		verify(response).addCookie(captor.capture());

		assertEquals("jwt", captor.getValue().getName());
		assertEquals(jwt, captor.getValue().getValue());

	}

	@Test
	public void givenNoJWT_whenLogin_thenReturnsError() throws Exception {
		when(jwtService.generateJWT(loginDTO.getUsername())).thenReturn(null);
		
		
		assertThrows(ResourceNotFound.class, () -> {
			authenticationController.loginUser(loginDTO, response);
		});
	}
	
//	@Test
//	public void givenAuthenticationException_whenLogin_thenReturnErrorMessage() throws Exception {
//		when(authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())))
//				.thenThrow(new RuntimeException());
//
//		ResponseEntity<ApiResponseDTO<String>> responseLogin = authenticationController.loginUser(loginDTO, response);
//
//
//		assertEquals(HttpStatus.UNAUTHORIZED, responseLogin.getStatusCode());
//
//	}

	@Test
	public void whenRegister_thenReturnOk() throws Exception {
		ResponseEntity<?> responseRegister = authenticationController.registerUser(registerDTO);

		assertEquals(HttpStatus.OK, responseRegister.getStatusCode());
	}

	@Test
	public void givenRightUser_whenRegister_thenReturnsOk() throws Exception {
		
		ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);

		authenticationController.registerUser(registerDTO);

		verify(userRepository).save(captor.capture());
		assertEquals(registerDTO.getUsername(), captor.getValue().getUsername());
		assertEquals(registerDTO.getEmail(), captor.getValue().getEmail());
	}
	
//	@Test
//	public void whenRegister_thenReturnError() throws Exception {
//		when(userRepository.save(any(Users.class))).thenThrow(new DataIntegrityViolationException(""));
//		
//		ResponseEntity<?> registerResponse = authenticationController.registerUser(registerDTO);
//		
//		assertEquals(HttpStatus.UNAUTHORIZED, registerResponse.getStatusCode());
//	}


}

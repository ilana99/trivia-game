package com.fsp.trivia;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fsp.trivia.authentication.LoginDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerIntegrationTest {
	
	private MockMvc mockMvc;

	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	
	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).alwaysDo(MockMvcResultHandlers.print()).build();
	}
	
	@Test
	public void whenLogin_thenReturnsOK() throws Exception {		
		System.out.println("login returns ok");	
		mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"username\": \"test\", \"password\": \"test\"}"))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Logged in")));
//		.andExpect(content().string("logged in"));
	}
	
	@Test
	public void givenJWTCookie_whenLogin_thenReturnOK() throws Exception {	
		Cookie jwtCookie = new Cookie("jwt", "jwt");
		
		mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"username\": \"test\", \"password\": \"test\"}")
		.cookie(jwtCookie))
		.andExpect(status().isOk())
		.andExpect(cookie().exists("jwt"));
		
	}
	
	@Test
	public void givenMissingParameter_whenLoginUser_thenReturnError() throws Exception {
		System.out.println("register error");	
		mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"username\": \"\", \"password\": \"test\"}"))
		.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void whenRegisterUser_thenReturnOK() throws Exception {
		System.out.println("register returns ok");	
		mockMvc.perform(post("/register")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"username\": \"test3\", \"password\": \"test\", \"email\": \"test3@gmail.com\"}"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void givenMissingParameter_whenRegisterUser_thenReturnError() throws Exception {
		System.out.println("register error");	
		mockMvc.perform(post("/register")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"username\": \"test4\", \"password\": \"test\", \"email\": \"\"}"))
		.andExpect(status().isBadRequest());
	}
	
}

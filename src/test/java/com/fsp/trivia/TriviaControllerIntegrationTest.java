package com.fsp.trivia;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fsp.trivia.trivia.TriviaService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test") 
public class TriviaControllerIntegrationTest {

	private MockMvc mockMvc;
	
//	@MockitoBean
//	private TriviaService triviaService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	
	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).alwaysDo(MockMvcResultHandlers.print()).build();
	}

	
	@Test
	public void whenStart_thenReturnsQuestions() throws Exception {
	    mockMvc.perform(get("/trivia/start"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.data").isNotEmpty());
	}
	
	
	@Test
	public void whenValidateAnswer_thenReturnsTrueAnswer() throws Exception {
		mockMvc.perform(get("/trivia/validate")
		.param("questionId", "1")
		.param("answerId", "1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.questionId").value("1"))
		.andExpect(jsonPath("$.data.answerId").value("1"))
		.andExpect(jsonPath("$.data.correctAnswerId").value("1"))
		.andExpect(jsonPath("$.data.status").value("true"));
	}
	
	@Test
	public void whenValidateAnswer_thenReturnsWrongAnswer() throws Exception {
		mockMvc.perform(get("/trivia/validate")
		.param("questionId", "1")
		.param("answerId", "2"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.status").value("false"));
	}
	
	@Test
	public void givenNonExistentQuestionId_whenValidateAnswer_thenReturnsError() throws Exception {
		mockMvc.perform(get("/trivia/validate")
		.param("questionId", "123")
		.param("answerId", "1"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void whenSetScore_thenReturnsOk() throws Exception {
		mockMvc.perform(post("/trivia/score")
		.param("userId", "1")
		.param("date", "2025-04-23")
		.param("score", "5")
		.param("numberOfQuestions", "5"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void givenNoUser_whenSetScore_thenReturnsError() throws Exception {
		mockMvc.perform(post("/trivia/score")
		.param("userId", "")
		.param("date", "2025-04-23")
		.param("score", "5")
		.param("numberOfQuestions", "5"))
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void givenWrongDateType_whenSetScore_thenReturnsError() throws Exception {
		mockMvc.perform(post("/trivia/score")
		.param("userId", "10")
		.param("date", "2025/04/23")
		.param("score", "5")
		.param("numberOfQuestions", "5"))
		.andExpect(status().is4xxClientError());
	}

}

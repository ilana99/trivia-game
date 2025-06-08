package com.fsp.trivia;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fsp.trivia.answers.Answer;
import com.fsp.trivia.errors.ResourceNotFound;
import com.fsp.trivia.games.Games;
import com.fsp.trivia.games.GamesRepository;
import com.fsp.trivia.questions.Question;
import com.fsp.trivia.questions.QuestionDTO;
import com.fsp.trivia.questions.QuestionRepository;
import com.fsp.trivia.trivia.ApiResponseDTO;
import com.fsp.trivia.trivia.SetScoreDTO;
import com.fsp.trivia.trivia.ValidateAnswerDTO;
import com.fsp.trivia.trivia.ValidationDTO;
import com.fsp.trivia.trivia.TriviaController;
import com.fsp.trivia.trivia.TriviaService;
import com.fsp.trivia.user.UserRepository;
import com.fsp.trivia.user.Users;

import io.jsonwebtoken.lang.Collections;



@ExtendWith(MockitoExtension.class)
public class TriviaControllerTest {


	@InjectMocks
	private TriviaController triviaController;

	@Mock
	private TriviaService triviaService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private GamesRepository gamesRepository;
	@Mock
	private QuestionRepository questionRepository;
	
	private Question question1;
	private List<Question> questions;
	private Answer answer1;
	private Users mockUser;
	private SetScoreDTO setScoreDTO;
	
	
	@BeforeEach
	public void setUp() {
		question1 = new Question();
		question1.setText("Test");
		question1.setId(1);
		
		answer1 = new Answer();
		answer1.setText("test");
		answer1.setId(1);
		answer1.setCorrect(true);
		answer1.setQuestion(question1);
		
		Answer answer2 = new Answer();
		answer2.setText("test");
		answer2.setId(2);
		answer2.setCorrect(false);
		answer2.setQuestion(question1);
		
		Answer answer3 = new Answer();
		answer3.setText("test");
		answer3.setId(3);
		answer3.setCorrect(false);
		answer3.setQuestion(question1);
		
		List<Answer> question1Answers = Arrays.asList(answer1, answer2, answer3);
		question1.setAnswers(question1Answers);
		
		Question question2 = new Question();
		question2.setText("Test");
		question2.setId(2);
		
		
		Answer answer4 = new Answer();
		answer4.setText("test");
		answer4.setId(4);
		answer4.setCorrect(false);
		answer4.setQuestion(question2);
		
		Answer answer5 = new Answer();
		answer5.setText("test");
		answer5.setId(5);
		answer5.setCorrect(false);
		answer5.setQuestion(question2);
		
		Answer answer6 = new Answer();
		answer6.setText("test");
		answer6.setId(6);
		answer6.setCorrect(true);
		answer6.setQuestion(question2);
		
		List<Answer> question2Answers = Arrays.asList(answer4, answer5, answer6);
		question2.setAnswers(question2Answers);
		
		questions = Arrays.asList(question1, question2);
		

		mockUser = new Users();
		mockUser.setId(2);
		
		setScoreDTO = new SetScoreDTO();
		setScoreDTO.setNumberOfQuestions(10);
		setScoreDTO.setScore(5);
		setScoreDTO.setUserId(2);
		setScoreDTO.setDate(Date.valueOf("2025-05-12"));
		
	}

	
	@Test
	public void whenStartGame_thenReturnQuestionsAndAnswers() throws Exception {
			
		when(triviaService.getQuestionsWithAnswers()).thenReturn(questions);
		

		ResponseEntity<ApiResponseDTO<List<QuestionDTO>>> response = triviaController.getQuestionsWithAnswers();
		

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	
	}
	
	@Test
	public void whenStartGame_thenReturn2QuestionsAnd3Answers() throws Exception {			
		when(triviaService.getQuestionsWithAnswers()).thenReturn(questions);
		

		ResponseEntity<ApiResponseDTO<List<QuestionDTO>>> response = triviaController.getQuestionsWithAnswers();
		
		
		assertEquals(2, response.getBody().getData().size());
		assertEquals(3, response.getBody().getData().getFirst().getAnswers().size());
	
	}
	
	@Test
	public void givenDTOStream_whenStartGame_thenReturnQuestionsAndAnswers() throws Exception {			
		when(triviaService.getQuestionsWithAnswers()).thenReturn(questions);
		

		ResponseEntity<ApiResponseDTO<List<QuestionDTO>>> response = triviaController.getQuestionsWithAnswers();
		
	
		assertEquals(question1.getId(), response.getBody().getData().getFirst().getId());
		assertEquals(question1.getAnswers().getFirst().getText(), response.getBody().getData().getFirst().getAnswers().getFirst().getAnswer());
	
	}

	
	@Test
	public void whenStartGame_thenTriviaServiceReturnsError() throws Exception {
		
		when(triviaService.getQuestionsWithAnswers()).thenReturn(Collections.emptyList());
		
		
		assertThrows(ResourceNotFound.class, () -> {
			triviaController.getQuestionsWithAnswers();
		});
		
	}

	


	@Test
	public void whenValidatingAnswer_thenReturnsGoodAnswer() throws Exception {
		ValidateAnswerDTO validateAnswerDTO = new ValidateAnswerDTO();
		validateAnswerDTO.setQuestionId(1);
		validateAnswerDTO.setAnswerId(1);
		
		when(questionRepository.findById(validateAnswerDTO.getQuestionId())).thenReturn(Optional.of(question1));
		
		ResponseEntity<ApiResponseDTO<ValidationDTO>> response = triviaController.validateAnswer(validateAnswerDTO);
		
		assertTrue(response.getBody().getData().isStatus());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	
	@Test
	public void givenIncorrectAnswer_whenValidatingAnswer_thenReturnsWrongAnswer() throws Exception {
		ValidateAnswerDTO validateAnswerDTO = new ValidateAnswerDTO();
		validateAnswerDTO.setQuestionId(1);
		validateAnswerDTO.setAnswerId(2);
		
		when(questionRepository.findById(validateAnswerDTO.getQuestionId())).thenReturn(Optional.of(question1));
		
		ResponseEntity<ApiResponseDTO<ValidationDTO>> response = triviaController.validateAnswer(validateAnswerDTO);
		
		assertFalse(response.getBody().getData().isStatus());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
	

	@Test
	public void givenQuestionNotFound_whenValidatingAnswer_thenReturnsUnauthorized() throws Exception {
		ValidateAnswerDTO validateAnswerDTO = new ValidateAnswerDTO();
		validateAnswerDTO.setQuestionId(1);
		validateAnswerDTO.setAnswerId(2);
	
		
		when(questionRepository.findById(validateAnswerDTO.getQuestionId())).thenReturn(Optional.empty());
		
		
		assertThrows(ResourceNotFound.class, () -> {
			triviaController.validateAnswer(validateAnswerDTO);
		});
			
	}
	
	
	
	@Test
	public void whenSettingScore_thenReturnsOK() throws Exception {

		
		when(userRepository.findById(setScoreDTO.getUserId())).thenReturn(Optional.of(mockUser));

		
		ResponseEntity<ApiResponseDTO<String>> response = triviaController.setScore(setScoreDTO);
		
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(gamesRepository).save(any(Games.class));
		
	}
	
	
	@Test
	public void givenNonExistentUser_whenSettingScore_thenReturnsUnauthorized() throws Exception {
		SetScoreDTO setScoreDTO = new SetScoreDTO();
		setScoreDTO.setNumberOfQuestions(10);
		setScoreDTO.setScore(5);
		setScoreDTO.setUserId(124);
		setScoreDTO.setDate(Date.valueOf("2025-05-12"));
	
		
		
		assertThrows(ResourceNotFound.class, () -> {
			triviaController.setScore(setScoreDTO);
		});
		
	}
	
//	@Test
//	public void givenWrongDateFormat_whenSettingScore_thenThrowsUnauthorized() throws Exception {
//		SetScoreDTO setScoreDTO = new SetScoreDTO();
//		setScoreDTO.setNumberOfQuestions(10);
//		setScoreDTO.setScore(5);
//		setScoreDTO.setUserId(1);
//		setScoreDTO.setDate(Date.valueOf("2025/05/12"));
//	
//		
//		assertThrows(HttpMessageNotReadableException.class, () -> {
//			triviaController.setScore(setScoreDTO);
//		});
//		
//	}
	


}

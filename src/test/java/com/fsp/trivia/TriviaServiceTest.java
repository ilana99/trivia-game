package com.fsp.trivia;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fsp.trivia.answers.Answer;
import com.fsp.trivia.answers.AnswerRepository;
import com.fsp.trivia.questions.Question;
import com.fsp.trivia.questions.QuestionRepository;
import com.fsp.trivia.trivia.TriviaService;

import io.jsonwebtoken.lang.Collections;

@ExtendWith(MockitoExtension.class)
public class TriviaServiceTest {
	
	@Spy
	@InjectMocks
	private TriviaService triviaService;
	
	@Mock
	private QuestionRepository questionRepository;
	@Mock
	private AnswerRepository answerRepository;
	
	
	private Question question1;
	private Answer answer1;
	private List<Question> questions;
	private List<Answer> answers;
	
	
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
		
		Question question3 = new Question();
		question3.setText("Test");
		question3.setId(3);
		

		Answer answer7 = new Answer();
		answer7.setText("test");
		answer7.setId(7);
		answer7.setCorrect(false);
		answer7.setQuestion(question3);
		
		Answer answer8 = new Answer();
		answer8.setText("test");
		answer8.setId(8);
		answer8.setCorrect(false);
		answer8.setQuestion(question3);
		
		Answer answer9 = new Answer();
		answer9.setText("test");
		answer9.setId(9);
		answer9.setCorrect(true);
		answer9.setQuestion(question3);
		
		List<Answer> question3Answers = Arrays.asList(answer7, answer8, answer9);
		question3.setAnswers(question3Answers);
		
		Question question4 = new Question();
		question4.setText("Test");
		question4.setId(4);
		

		Answer answer10 = new Answer();
		answer10.setText("test");
		answer10.setId(10);
		answer10.setCorrect(false);
		answer10.setQuestion(question4);
		
		Answer answer11 = new Answer();
		answer11.setText("test");
		answer11.setId(11);
		answer11.setCorrect(false);
		answer11.setQuestion(question4);
		
		Answer answer12 = new Answer();
		answer12.setText("test");
		answer12.setId(12);
		answer12.setCorrect(true);
		answer12.setQuestion(question4);
		
		List<Answer> question4Answers = Arrays.asList(answer10, answer11, answer12);
		question4.setAnswers(question4Answers);
		
		
		Question question5 = new Question();
		question5.setText("Test");
		question5.setId(5);
		

		Answer answer13 = new Answer();
		answer13.setText("test");
		answer13.setId(13);
		answer13.setCorrect(false);
		answer13.setQuestion(question5);
		
		Answer answer14 = new Answer();
		answer14.setText("test");
		answer14.setId(14);
		answer14.setCorrect(false);
		answer14.setQuestion(question5);
		
		Answer answer15 = new Answer();
		answer15.setText("test");
		answer15.setId(15);
		answer15.setCorrect(true);
		answer15.setQuestion(question5);

		List<Answer> question5Answers = Arrays.asList(answer13, answer14, answer15);
		question5.setAnswers(question5Answers);
		
		questions = Arrays.asList(question1, question2, question3, question4, question5);
		
		answers = Arrays.asList(answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9, answer10, answer11, answer12, answer13, answer14, answer15);
	}
	
	@Test
	public void whenGetRandomQuestions_thenReturnListOf5Questions() throws Exception {
		when(questionRepository.findAll()).thenReturn(questions);
		
		List<Question> fiveQuestions = triviaService.getRandomQuestions();
		
		assertEquals(5, fiveQuestions.size());
	}
	
	@Test
	public void givenAnEmptyListOfQuestions_whenGetRandomQuestions_thenReturnRuntimeException() throws Exception {
		when(questionRepository.findAll()).thenReturn(Collections.emptyList());
	
		
		assertThrows(RuntimeException.class, () -> {
	        triviaService.getRandomQuestions();
	    });
	}
	
	
	@Test
	public void whenGetAllAnswers_thenReturnAllAnswers() throws Exception {
		when(answerRepository.findAll()).thenReturn(answers);
		
		List<Answer> allAnswers = triviaService.getAllAnswers(questions);
		
		assertEquals(15, allAnswers.size());
	}
	

	@Test
	public void whenGetAllAnswers_thenReturnAllAnswersWithIds() throws Exception {
		when(answerRepository.findAll()).thenReturn(answers);
		
		List<Answer> allAnswers = triviaService.getAllAnswers(questions);
		
		assertEquals(answer1.getId(), allAnswers.getFirst().getId());
	}
	
	@Test
	public void givenAnEmptyListOfQuestions_whenGetAllAnswers_thenReturnsRuntimeException() throws Exception {
		when(answerRepository.findAll()).thenReturn(Collections.emptyList());
		
		assertThrows(RuntimeException.class, () -> {
	        triviaService.getAllAnswers(questions);
	    });
	}
	
	@Test
	public void whenPickAnswers_thenReturn3Answers() throws Exception {
		List<Answer> selectedAnswers = triviaService.pickAnswers(answers);
		
		
		assertEquals(3, selectedAnswers.size());		
	}
	
	
	@Test
	public void whenPickAnswers_thenReturnAnswersWithCorrectAnswerOption() throws Exception {
		List<Answer> selectedAnswers = triviaService.pickAnswers(answers);
		
		Long correctAnswers = selectedAnswers.stream()
				.filter(Answer::isCorrect)
				.count();
		
		assertNotNull(correctAnswers);
		assertEquals(1, correctAnswers);
	}
	
	@Test
	public void givenAnEmptyListOfAnswers_whenPickAnswers_thenReturnsError() throws Exception {
		assertThrows(RuntimeException.class, () -> {
			triviaService.pickAnswers(Collections.emptyList());
		});
			
	}
		
	
	
	@Test
	public void whenGetQuestionsWithAnswers_thenReturnRandomQuestions() throws Exception {
		doReturn(questions).when(triviaService).getRandomQuestions();
		doReturn(answers).when(triviaService).getAllAnswers(questions);
					
		List<Question> randomQuestions = triviaService.getQuestionsWithAnswers();
		
		assertEquals(5, randomQuestions.size());
		assertEquals(3, randomQuestions.getFirst().getAnswers().size());
		
	}
	
	@Test
	public void givenEmptyListOfQuestions_whenGetQuestionsWithAnswers_thenReturnError() throws Exception {
		doReturn(Collections.emptyList()).when(triviaService).getRandomQuestions();

					
		assertThrows(RuntimeException.class, () -> {
			triviaService.getQuestionsWithAnswers();
		});
		
	}
	

	

}

package com.fsp.trivia.trivia;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsp.trivia.answers.Answer;
import com.fsp.trivia.answers.AnswerDTO;
import com.fsp.trivia.errors.GeneralException;
import com.fsp.trivia.errors.ResourceNotFound;
import com.fsp.trivia.games.Games;
import com.fsp.trivia.games.GamesRepository;
import com.fsp.trivia.questions.Question;
import com.fsp.trivia.questions.QuestionDTO;
import com.fsp.trivia.questions.QuestionRepository;
import com.fsp.trivia.user.UserRepository;
import com.fsp.trivia.user.Users;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/trivia")
public class TriviaController {
	
	@Autowired
    private TriviaService triviaService;
	@Autowired
	 private GamesRepository gamesRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private QuestionRepository questionRepository;
	

	public TriviaController(TriviaService triviaService, GamesRepository gamesRepository, UserRepository userRepository,
			QuestionRepository questionRepository) {
		this.triviaService = triviaService;
		this.gamesRepository = gamesRepository;
		this.userRepository = userRepository;
		this.questionRepository = questionRepository;
	}



	
	@GetMapping(path = "/start")
	public ResponseEntity<ApiResponseDTO<List<QuestionDTO>>> getQuestionsWithAnswers() {

			List<Question> questions = triviaService.getQuestionsWithAnswers();
			
			if (questions.isEmpty()) {
				throw new ResourceNotFound("No questions found");
			}
			
			List<QuestionDTO> questionDtos = questions.stream().map(question -> {
				QuestionDTO questionDto = new QuestionDTO();
				questionDto.setId(question.getId());
				questionDto.setQuestion(question.getText());
				List<AnswerDTO> answerDtos = question.getAnswers().stream()
						  .map(answer -> {
							  AnswerDTO answerDto = new AnswerDTO();
							  answerDto.setId(answer.getId());
							  answerDto.setAnswer(answer.getText());
							  return answerDto;
						  })
						  .collect(Collectors.toList());
				  
				 questionDto.setAnswers(answerDtos);
			     return questionDto;
			}).collect(Collectors.toList());
			
			return ResponseEntity.ok(new ApiResponseDTO<>(questionDtos));
	}
	

	
	@GetMapping(path = "/validate")
	public ResponseEntity<ApiResponseDTO<ValidationDTO>> validateAnswer(@Valid @RequestBody ValidateAnswerDTO validateAnswerDTO) {
	
			ValidationDTO response = new ValidationDTO();
			
			Optional<Question> optionalQuestion = questionRepository.findById(validateAnswerDTO.getQuestionId());

		    Question question = optionalQuestion.orElseThrow(() -> 
		        new ResourceNotFound("Question not found with ID: " + validateAnswerDTO.getQuestionId())
		    );
		
			boolean isCorrect = question.getAnswers().stream()
					 .anyMatch(answer -> answer.getId().equals(validateAnswerDTO.getAnswerId()) && answer.isCorrect());
			
			Integer correctAnswer = question.getAnswers().stream()
					.filter(Answer::isCorrect)
					.map(Answer::getId)
					.findFirst()
					.orElseThrow(() -> new GeneralException("No correct answer found."));
		
					
			response.setAnswerId(validateAnswerDTO.getAnswerId());
			response.setQuestionId(validateAnswerDTO.getQuestionId());
			response.setCorrectAnswerId(correctAnswer);
			
			if (isCorrect) {
				response.setStatus(true);
			} else {
				response.setStatus(false);
			}
			
			return ResponseEntity.ok(new ApiResponseDTO<>(response));		 
	}


	@PostMapping(path= "/score")
 	public ResponseEntity<ApiResponseDTO<String>> setScore(@Valid @RequestBody SetScoreDTO setScoreDTO) {

			Users user = userRepository.findById(setScoreDTO.getUserId())
					.orElseThrow(() -> new ResourceNotFound("No user found with ID: " + setScoreDTO.getUserId()));
			Games newGame = new Games();
			newGame.setUser(user);
			newGame.setScore(setScoreDTO.getScore());
			newGame.setNumberOfQuestions(setScoreDTO.getNumberOfQuestions());
			newGame.setDatePlayed(setScoreDTO.getDate());
			gamesRepository.save(newGame);
			return ResponseEntity.ok(new ApiResponseDTO<>("score set"));
	}

}

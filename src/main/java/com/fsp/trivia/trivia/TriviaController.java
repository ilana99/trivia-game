package com.fsp.trivia.trivia;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fsp.trivia.answers.Answer;
import com.fsp.trivia.answers.AnswerDTO;
import com.fsp.trivia.questions.ApiResponseDTO;
import com.fsp.trivia.questions.Question;
import com.fsp.trivia.questions.QuestionDTO;
import com.fsp.trivia.questions.QuestionRepository;
import com.fsp.trivia.questions.ValidationDTO;
import com.fsp.trivia.user.Games;
import com.fsp.trivia.user.GamesRepository;
import com.fsp.trivia.user.UserRepository;
import com.fsp.trivia.user.Users;

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
	public ApiResponseDTO<List<QuestionDTO>> getQuestionsWithAnswers() {
		List<Question> questions = triviaService.getQuestionsWithAnswers();
		
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
		
		 return new ApiResponseDTO<>(questionDtos);
	}
	
	
//	@GetMapping(path = "/validate")
//	public ValidationDTO validateAnswer( @RequestParam Integer questionId,
//            @RequestParam Integer answerId) {
//		return triviaService.validateAnswer(questionId, answerId);
//	}
	
	@GetMapping(path = "/validate")
	public ResponseEntity<ValidationDTO> validateAnswer( @RequestParam Integer questionId,
            @RequestParam Integer answerId) {
		try {
			ValidationDTO response = new ValidationDTO();
			
			Optional<Question> optionalQuestion = questionRepository.findById(questionId);

		    Question question = optionalQuestion.orElseThrow(() -> 
		        new IllegalArgumentException("Invalid question ID: " + questionId)
		    );
		
			boolean isCorrect = question.getAnswers().stream()
					 .anyMatch(answer -> answer.getId().equals(answerId) && answer.isCorrect());
			
			Integer correctAnswer = question.getAnswers().stream()
					.filter(Answer::isCorrect)
					.map(Answer::getId)
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("No correct answer found."));
		
					
			response.setAnswerId(answerId);
			response.setQuestionId(questionId);
			response.setCorrectAnswerId(correctAnswer);
			
			if (isCorrect) {
				response.setStatus(true);
			} else {
				response.setStatus(false);
			}
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		
		 
	}

	
	@PostMapping(path= "/score")
 	public ResponseEntity<Void> setScore(@RequestParam Integer userId, @RequestParam Date date, @RequestParam Integer score, @RequestParam Integer numberOfQuestions) {
		try {
			Users user = userRepository.getReferenceById(userId);
			Games newGame = new Games();
			newGame.setUser(user);
			newGame.setScore(score);
			newGame.setNumberOfQuestions(numberOfQuestions);
			newGame.setDatePlayed(date);
			gamesRepository.save(newGame);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e){
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

}

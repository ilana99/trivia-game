package com.fsp.trivia.trivia;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsp.trivia.answers.Answer;
import com.fsp.trivia.answers.AnswerRepository;
import com.fsp.trivia.questions.Question;
import com.fsp.trivia.questions.QuestionRepository;

@Service
public class TriviaService {
	
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private QuestionRepository questionRepository;
	
	
	public List<Question> getRandomQuestions() {
		List<Question> allQuestions = (List<Question>) questionRepository.findAll();
		
		if (allQuestions.isEmpty() || allQuestions.size() < 5 ) {
			throw new RuntimeException();
	    }
	
		Collections.shuffle(allQuestions); 
	   
		List<Question> randomQuestions = allQuestions.subList(0, 5); 
		
	    
		return randomQuestions;
	}
	
	public List<Answer> getAllAnswers(List<Question> randomQuestions) {
		List<Integer> questionsIds = randomQuestions.stream()
				.map(Question::getId)
				.collect(Collectors.toList());
					
		
		List<Answer> allAnswers = (List<Answer>) answerRepository.findAll();
		
		if (allAnswers.isEmpty() || questionsIds.isEmpty()) {
			throw new RuntimeException();
	    }
		
		return allAnswers.stream()
                 .filter(answer -> questionsIds.contains(answer.getQuestion().getId()))
                 .collect(Collectors.toList());
	}
	
	//allows for different answers every time 
	public List<Answer> pickAnswers(List<Answer> answers) {
		List<Answer> correctAnswer = answers.stream()
				.filter(Answer::isCorrect)
				.collect(Collectors.toList());
		
		if (correctAnswer.isEmpty()) {
		    throw new RuntimeException("no correct answer");
		}
		
		
		List<Answer> otherAnswers = answers.stream()
				.filter(answer -> !answer.isCorrect())
				.collect(Collectors.toList());
		
		if (otherAnswers.isEmpty()) {
		    throw new RuntimeException("no other answer");
		}
		
		Collections.shuffle(otherAnswers);
		List<Answer> selectedAnswers = otherAnswers.subList(0, 2);
		
		selectedAnswers.add(correctAnswer.get(0));
		
		if (selectedAnswers.isEmpty() || selectedAnswers.size() < 1) {
		    throw new RuntimeException("no selected answer");
		}
		
		Collections.shuffle(selectedAnswers);
		

		
		return selectedAnswers;
				
	}
	
	public List<Question> getQuestionsWithAnswers() {
		List<Question> randomQuestions = getRandomQuestions();
		
		if (randomQuestions.isEmpty()) {
			throw new RuntimeException("no questions found");
		}
		
		List<Answer> allAnswers = getAllAnswers(randomQuestions);
		
		
		if (allAnswers.isEmpty()) {
			throw new RuntimeException("no answers found");
		}
		
		System.out.println(allAnswers);
		System.out.println(randomQuestions);
		
		Map<Integer, List <Answer>> answersByQuestionId = allAnswers.stream()
				.collect(Collectors.groupingBy(answer -> (Integer) answer.getQuestion().getId()));
		
		if (answersByQuestionId.isEmpty()) {
			throw new RuntimeException(); 
		}
		
		randomQuestions.forEach(question -> {
			 List<Answer> answersForQuestion = answersByQuestionId.get(question.getId());
	         List<Answer> selectedAnswers = pickAnswers(answersForQuestion);
	         question.setAnswers(selectedAnswers);      
		});
		

		
		return randomQuestions;
		
	}
	

}

package com.fsp.trivia.questions;

import java.util.List;

import com.fsp.trivia.answers.AnswerDTO;

public class QuestionDTO {

	private Integer id;
	
	private String question;

	private List<AnswerDTO> answers;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers;
	}
	
	
}

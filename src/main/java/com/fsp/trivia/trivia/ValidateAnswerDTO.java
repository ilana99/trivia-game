package com.fsp.trivia.trivia;

import jakarta.validation.constraints.NotNull;

public class ValidateAnswerDTO {
	
	@NotNull(message = "questionId cannot be empty")
	private Integer questionId;
	@NotNull(message = "answerId cannot be empty")
	private Integer answerId;
	
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public Integer getAnswerId() {
		return answerId;
	}
	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
	}

}

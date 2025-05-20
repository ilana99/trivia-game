package com.fsp.trivia.questions;

public class ValidationDTO {
	
	
	private Integer questionId;
	private Integer answerId;
	private Integer correctAnswerId;
	private boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getCorrectAnswerId() {
		return correctAnswerId;
	}

	public void setCorrectAnswerId(Integer correctAnswerId) {
		this.correctAnswerId = correctAnswerId;
	}

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

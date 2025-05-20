package com.fsp.trivia.trivia;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


import jakarta.validation.constraints.NotNull;

public class SetScoreDTO {

	@NotNull(message = "userId cannot be empty")
	private Integer userId;
	@NotNull(message = "date cannot be empty")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;
	@NotNull(message = "score cannot be empty")
	private Integer score;
	@NotNull(message = "numberOfQuestions cannot be empty")
	private Integer numberOfQuestions;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}
	public void setNumberOfQuestions(Integer numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}
	
}

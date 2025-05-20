package com.fsp.trivia.questions;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {

	private T data;
	private String message;
	
	public ApiResponseDTO(T data) {
		this.data = data;
	}
	
	public ApiResponseDTO(String message) {
		this.message = message;
	}
	

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}

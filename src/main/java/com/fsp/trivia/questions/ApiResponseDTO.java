package com.fsp.trivia.questions;

public class ApiResponseDTO<T> {

	private T data;
	
	public ApiResponseDTO(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
}

package com.fsp.trivia.errors;

import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;




@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = GeneralException.class) 
	public ResponseEntity<ExceptionDTO> handleGeneralException(GeneralException ex) {
		ExceptionDTO response = new ExceptionDTO("INTERNAL_SERVER_ERROR", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = ResourceNotFound.class)
	public ResponseEntity<ExceptionDTO> handleResourceNotFound(ResourceNotFound ex) {
		ExceptionDTO response = new ExceptionDTO("RESOURCE_NOT_FOUND", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}



	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String errorMessage = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getDefaultMessage())
				.collect(Collectors.joining("; "));

		ExceptionDTO response = new ExceptionDTO("VALIDATION_ERROR", errorMessage);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		if (ex.getCause() instanceof InvalidFormatException invalidEx) {

			String fieldName = invalidEx.getPath().stream()
					.map(ref -> ref.getFieldName())
					.collect(Collectors.joining("."));

			String targetType = invalidEx.getTargetType().getSimpleName();
			
			String customMessage;

			if ("date".equals(fieldName)) {
				targetType = "YYYY-MM-DD";
				customMessage = String.format(
						"Invalid format for field '%s'. Expected format: %s",
						fieldName, targetType
						);
			} else {
				customMessage = String.format(
						"Invalid format for field '%s'. Expected type: %s",
						fieldName, targetType
						);
			}

			ExceptionDTO response = new ExceptionDTO("FORMAT_ERROR", customMessage);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} 
		ExceptionDTO response = new ExceptionDTO("ERROR", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);



	}

}
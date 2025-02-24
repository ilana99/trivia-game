package com.fsp.trivia.questions;

import org.springframework.data.repository.ListCrudRepository;

public interface QuestionRepository extends ListCrudRepository<Question, Integer> {
	
}

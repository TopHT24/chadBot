package com.example.v1.service.llm;

import com.example.v1.dto.llm.GeneratedQuestionSet;
import com.example.v1.dto.llm.QuestionGenerationInput;

/**
 * Generates questions (with rubrics) from the student's material.
 * The real implementation will call the LLM API; StubQuestionGenerator is used until then.
 */
public interface QuestionGenerator {

	GeneratedQuestionSet generate(QuestionGenerationInput input);
}

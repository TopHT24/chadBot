package com.example.v1.dto.llm;

import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionType;

import java.util.List;

/**
 * Everything the question-generation prompt is filled with.
 *
 * @param types allowed question types; empty = any
 */
public record QuestionGenerationInput(
		String sourceText,
		List<QuestionType> types,
		Difficulty difficulty,
		int count) {
}

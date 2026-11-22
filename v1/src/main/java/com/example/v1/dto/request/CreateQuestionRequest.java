package com.example.v1.dto.request;

import com.example.v1.model.enums.BloomLevel;
import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * A teacher-written question, or edits to a generated one.
 * The rubric is required when requiresExplanation is true (checked in the service).
 */
public record CreateQuestionRequest(
		@NotNull Long courseId,
		Long objectiveId,
		@NotNull QuestionType type,
		@NotBlank String stem,
		List<String> options,
		@NotBlank String correctAnswer,
		boolean requiresExplanation,
		String explanationPrompt,
		Difficulty difficulty,
		BloomLevel bloomLevel,
		@Valid RubricRequest rubric,
		@NotNull Long createdBy) {
}

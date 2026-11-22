package com.example.v1.dto.request;

import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Asks the LLM to generate DRAFT questions from course material.
 */
public record GenerateQuestionsRequest(
		@NotNull Long courseId,
		@NotEmpty List<Long> sourceMaterialIds,
		List<Long> objectiveIds,
		@NotEmpty List<QuestionType> types,
		Difficulty difficulty,
		@Min(1) @Max(20) int count,
		@NotNull Long requestedBy) {
}

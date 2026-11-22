package com.example.v1.dto.request;

import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Asks the LLM to generate a test from some of the student's materials.
 */
public record GenerateExamRequest(
		@NotNull Long userId,
		@NotEmpty List<Long> sourceMaterialIds,
		/* optional: a title is made up from the materials when missing */
		String title,
		@Min(1) @Max(20) int questionCount,
		/* optional: any type when empty */
		List<QuestionType> types,
		Difficulty difficulty,
		/* optional: no time limit when null */
		@Positive Integer timeLimitMinutes) {
}

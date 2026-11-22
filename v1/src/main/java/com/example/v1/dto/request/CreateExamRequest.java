package com.example.v1.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.util.List;

/**
 * Question order in the list is the exam order.
 */
public record CreateExamRequest(
		@NotNull Long courseId,
		@NotBlank String title,
		String instructions,
		@Positive Integer timeLimitMinutes,
		@Min(1) int maxAttempts,
		boolean shuffleQuestions,
		@Min(0) int minExplanationWords,
		@Min(0) int maxFollowUps,
		Instant opensAt,
		Instant closesAt,
		@NotEmpty List<@Valid ExamQuestionRequest> questions,
		@NotNull Long createdBy) {
}

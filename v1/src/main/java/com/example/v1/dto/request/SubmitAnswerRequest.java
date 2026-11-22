package com.example.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Saves (or updates) the answer and explanation for one question of an attempt.
 * The explanation's minimum length depends on the exam, so the service checks it.
 */
public record SubmitAnswerRequest(
		@NotNull Long questionId,
		@NotBlank String answer,
		String explanation,
		@PositiveOrZero Integer timeSpentSeconds) {
}

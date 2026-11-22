package com.example.v1.dto.request;

import com.example.v1.model.enums.QuestionStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Teacher approves or rejects a generated question.
 */
public record ReviewQuestionRequest(
		@NotNull QuestionStatus status,
		String reviewNote,
		@NotNull Long reviewerId) {
}

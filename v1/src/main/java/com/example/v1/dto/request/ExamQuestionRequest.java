package com.example.v1.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ExamQuestionRequest(
		@NotNull Long questionId,
		@PositiveOrZero double answerPoints,
		@PositiveOrZero double explanationPoints) {
}

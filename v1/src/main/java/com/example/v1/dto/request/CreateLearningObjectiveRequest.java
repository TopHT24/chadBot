package com.example.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLearningObjectiveRequest(
		@NotNull Long courseId,
		@NotBlank String description) {
}

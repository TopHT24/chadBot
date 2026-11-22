package com.example.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(
		@NotBlank String name,
		String description,
		@NotNull Long teacherId) {
}

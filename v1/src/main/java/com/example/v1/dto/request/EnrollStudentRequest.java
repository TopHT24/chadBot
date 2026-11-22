package com.example.v1.dto.request;

import jakarta.validation.constraints.NotNull;

public record EnrollStudentRequest(
		@NotNull Long courseId,
		@NotNull Long studentId) {
}

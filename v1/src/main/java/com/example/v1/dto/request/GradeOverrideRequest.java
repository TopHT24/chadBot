package com.example.v1.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Explanation scores are stored on a 0..1 scale.
 */
public record GradeOverrideRequest(
		@NotNull Long teacherId,
		@DecimalMin("0.0") @DecimalMax("1.0") double newScore,
		@NotBlank String reason) {
}

package com.example.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RubricCriterionRequest(
		@NotBlank String keyConcept,
		String description,
		@Positive double points,
		boolean required) {
}

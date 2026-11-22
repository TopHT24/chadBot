package com.example.v1.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * studentId will come from the logged-in user once authentication exists.
 */
public record StartAttemptRequest(
		@NotNull Long examId,
		@NotNull Long studentId) {
}

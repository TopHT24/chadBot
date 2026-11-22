package com.example.v1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Pasted text. Uploaded files come in as multipart instead (see MaterialController).
 */
public record CreateSourceMaterialRequest(
		@NotNull Long userId,
		@NotBlank String title,
		@NotBlank String content) {
}

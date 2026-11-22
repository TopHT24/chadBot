package com.example.v1.dto.request;

import com.example.v1.model.enums.SourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSourceMaterialRequest(
		@NotNull Long courseId,
		@NotBlank String title,
		@NotNull SourceType type,
		/* Plain text for TEXT; extracted text for PDF/URL once upload handling exists. */
		@NotBlank String content,
		String fileRef,
		@NotNull Long uploadedBy) {
}

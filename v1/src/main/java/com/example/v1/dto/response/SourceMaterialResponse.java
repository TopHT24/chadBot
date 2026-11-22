package com.example.v1.dto.response;

import com.example.v1.model.SourceMaterial;
import com.example.v1.model.enums.SourceType;

import java.time.Instant;

/**
 * Leaves out the full content (can be large); returns a short preview instead.
 */
public record SourceMaterialResponse(Long id, Long userId, String title, SourceType type,
		String preview, int contentLength, Instant createdAt) {

	private static final int PREVIEW_LENGTH = 200;

	public static SourceMaterialResponse from(SourceMaterial s) {
		String content = s.getContent() == null ? "" : s.getContent();
		String preview = content.length() <= PREVIEW_LENGTH ? content : content.substring(0, PREVIEW_LENGTH) + "…";
		return new SourceMaterialResponse(s.getId(), s.getUserId(), s.getTitle(), s.getType(),
				preview, content.length(), s.getCreatedAt());
	}
}

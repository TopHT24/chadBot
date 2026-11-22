package com.example.v1.dto.response;

import com.example.v1.model.Exam;
import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.ExamStatus;

import java.time.Instant;
import java.util.List;

/**
 * A generated test as listed for its owner (no questions; start an attempt to see them).
 */
public record ExamResponse(Long id, Long userId, String title, ExamStatus status, String errorMessage,
		List<Long> sourceMaterialIds, Difficulty difficulty, int questionCount, Integer timeLimitMinutes,
		Instant createdAt) {

	public static ExamResponse from(Exam e, int questionCount) {
		return new ExamResponse(e.getId(), e.getUserId(), e.getTitle(), e.getStatus(), e.getErrorMessage(),
				List.copyOf(e.getSourceMaterialIds()), e.getDifficulty(), questionCount, e.getTimeLimitMinutes(),
				e.getCreatedAt());
	}
}

package com.example.v1.dto.response;

import com.example.v1.model.Attempt;
import com.example.v1.model.enums.AttemptStatus;

import java.time.Instant;

public record AttemptResponse(Long id, Long examId, Long studentId, int attemptNumber, AttemptStatus status,
		Instant startedAt, Instant submittedAt, Double answerScore, Double explanationScore, Double totalScore) {

	public static AttemptResponse from(Attempt a) {
		return new AttemptResponse(a.getId(), a.getExamId(), a.getStudentId(), a.getAttemptNumber(), a.getStatus(),
				a.getStartedAt(), a.getSubmittedAt(), a.getAnswerScore(), a.getExplanationScore(), a.getTotalScore());
	}
}

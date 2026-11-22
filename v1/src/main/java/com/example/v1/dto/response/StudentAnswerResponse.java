package com.example.v1.dto.response;

import com.example.v1.model.StudentAnswer;

import java.time.Instant;

/**
 * Confirmation of a saved answer. answerCorrect is left out on purpose: students
 * see results only after grading, via AttemptResultResponse.
 */
public record StudentAnswerResponse(Long id, Long attemptId, Long questionId, String answer, String explanation,
		int revisionCount, Instant submittedAt) {

	public static StudentAnswerResponse from(StudentAnswer a) {
		return new StudentAnswerResponse(a.getId(), a.getAttemptId(), a.getQuestionId(), a.getAnswer(),
				a.getExplanation(), a.getRevisionCount(), a.getSubmittedAt());
	}
}

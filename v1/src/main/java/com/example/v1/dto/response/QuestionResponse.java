package com.example.v1.dto.response;

import com.example.v1.model.Question;
import com.example.v1.model.enums.BloomLevel;
import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionOrigin;
import com.example.v1.model.enums.QuestionStatus;
import com.example.v1.model.enums.QuestionType;

import java.time.Instant;
import java.util.List;

/**
 * Teacher view of a question: includes the correct answer and the rubric.
 * Students get StudentQuestionView instead.
 */
public record QuestionResponse(Long id, Long courseId, Long objectiveId, QuestionType type, String stem,
		List<String> options, String correctAnswer, boolean requiresExplanation, String explanationPrompt,
		Difficulty difficulty, BloomLevel bloomLevel, QuestionOrigin origin, QuestionStatus status,
		String reviewNote, RubricResponse rubric, Instant createdAt) {

	/** @param rubric may be null when the question has none */
	public static QuestionResponse from(Question q, RubricResponse rubric) {
		return new QuestionResponse(q.getId(), q.getCourseId(), q.getObjectiveId(), q.getType(), q.getStem(),
				List.copyOf(q.getOptions()), q.getCorrectAnswer(), q.isRequiresExplanation(),
				q.getExplanationPrompt(), q.getDifficulty(), q.getBloomLevel(), q.getOrigin(), q.getStatus(),
				q.getReviewNote(), rubric, q.getCreatedAt());
	}
}

package com.example.v1.dto.llm;

import java.util.List;

/**
 * Everything the grading prompt is filled with. Built by the service from
 * Question, Rubric, RubricCriterion, StudentAnswer and FollowUp rows.
 */
public record ExplanationGradingInput(
		String questionStem,
		List<String> options,
		String correctAnswer,
		String studentAnswer,
		boolean answerCorrect,
		String studentExplanation,
		String referenceExplanation,
		List<Criterion> criteria,
		List<String> commonMisconceptions,
		List<PriorFollowUp> followUps) {

	public record Criterion(Long criterionId, String keyConcept, String description, boolean required) {
	}

	public record PriorFollowUp(String question, String studentReply) {
	}
}

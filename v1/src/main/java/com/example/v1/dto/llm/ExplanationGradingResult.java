package com.example.v1.dto.llm;

import java.util.List;

/**
 * Top-level response for an EXPLANATION_GRADING call.
 * Maps to an ExplanationEvaluation (and possibly a FollowUp).
 *
 * @param consistentWithAnswer does the reasoning actually support the answer the student chose?
 * @param confidence           0..1; low values send the evaluation to teacher review
 * @param followUpQuestion     a probing question when the explanation is vague, otherwise null
 */
public record ExplanationGradingResult(
		List<GradedCriterion> criteria,
		List<String> misconceptionsDetected,
		boolean consistentWithAnswer,
		String feedbackToStudent,
		double confidence,
		String followUpQuestion) {
}

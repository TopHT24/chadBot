package com.example.v1.dto.response;

import com.example.v1.model.CriterionResult;
import com.example.v1.model.ExplanationEvaluation;
import com.example.v1.model.GradeOverride;

import java.time.Instant;
import java.util.List;

/**
 * Teacher view of an LLM grade, with any overrides. effectiveScore is the latest
 * override if there is one, otherwise the LLM's score.
 */
public record ExplanationEvaluationResponse(Long id, Long studentAnswerId, List<CriterionResult> criteriaResults,
		List<String> misconceptionsDetected, double llmScore, double effectiveScore, Boolean consistentWithAnswer,
		String feedbackToStudent, double confidence, boolean needsTeacherReview, List<OverrideEntry> overrides,
		Instant createdAt) {

	public record OverrideEntry(Long teacherId, double previousScore, double newScore, String reason, Instant createdAt) {

		public static OverrideEntry from(GradeOverride g) {
			return new OverrideEntry(g.getTeacherId(), g.getPreviousScore(), g.getNewScore(), g.getReason(), g.getCreatedAt());
		}
	}

	/** @param overrides ordered oldest first */
	public static ExplanationEvaluationResponse from(ExplanationEvaluation e, List<GradeOverride> overrides) {
		double effective = overrides.isEmpty() ? e.getExplanationScore() : overrides.getLast().getNewScore();
		return new ExplanationEvaluationResponse(e.getId(), e.getStudentAnswerId(), List.copyOf(e.getCriteriaResults()),
				List.copyOf(e.getMisconceptionsDetected()), e.getExplanationScore(), effective,
				e.getConsistentWithAnswer(), e.getFeedbackToStudent(), e.getConfidence(), e.isNeedsTeacherReview(),
				overrides.stream().map(OverrideEntry::from).toList(), e.getCreatedAt());
	}
}

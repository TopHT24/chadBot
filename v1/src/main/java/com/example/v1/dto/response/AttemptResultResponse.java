package com.example.v1.dto.response;

import java.util.List;

/**
 * What a student sees after grading: per-question scores and feedback, without
 * internal fields such as LLM confidence or rubric points. The service assembles it.
 */
public record AttemptResultResponse(AttemptResponse attempt, List<QuestionResult> results) {

	public record QuestionResult(
			Long questionId,
			String stem,
			String yourAnswer,
			String yourExplanation,
			boolean answerCorrect,
			String correctAnswer,
			double answerPointsEarned,
			double explanationPointsEarned,
			double maxPoints,
			/* true when the answer is right but the reasoning doesn't support it */
			boolean reasoningMismatch,
			List<String> conceptsCovered,
			List<String> conceptsMissed,
			List<String> misconceptions,
			String feedback,
			List<FollowUpResponse> followUps) {
	}
}

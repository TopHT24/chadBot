package com.example.v1.dto.response;

import com.example.v1.model.Exam;
import com.example.v1.model.ExamQuestion;
import com.example.v1.model.enums.ExamStatus;

import java.time.Instant;
import java.util.List;

/**
 * Teacher view of an exam and its question list.
 */
public record ExamResponse(Long id, Long courseId, String title, String instructions, Integer timeLimitMinutes,
		int maxAttempts, boolean shuffleQuestions, int minExplanationWords, int maxFollowUps, ExamStatus status,
		Instant opensAt, Instant closesAt, List<Item> questions, double totalPoints) {

	public record Item(Long questionId, int position, double answerPoints, double explanationPoints) {

		public static Item from(ExamQuestion eq) {
			return new Item(eq.getQuestionId(), eq.getPosition(), eq.getAnswerPoints(), eq.getExplanationPoints());
		}
	}

	public static ExamResponse from(Exam e, List<ExamQuestion> questions) {
		List<Item> items = questions.stream().map(Item::from).toList();
		double total = items.stream().mapToDouble(i -> i.answerPoints() + i.explanationPoints()).sum();
		return new ExamResponse(e.getId(), e.getCourseId(), e.getTitle(), e.getInstructions(),
				e.getTimeLimitMinutes(), e.getMaxAttempts(), e.isShuffleQuestions(), e.getMinExplanationWords(),
				e.getMaxFollowUps(), e.getStatus(), e.getOpensAt(), e.getClosesAt(), items, total);
	}
}

package com.example.v1.dto.response;

import com.example.v1.model.Attempt;
import com.example.v1.model.Exam;

import java.time.Instant;
import java.util.List;

/**
 * What a student gets when starting or resuming an attempt.
 */
public record StudentExamView(Long examId, Long attemptId, String title, String instructions,
		Integer timeLimitMinutes, int minExplanationWords, Instant startedAt, List<StudentQuestionView> questions) {

	/** @param questions already in display order (shuffled by the service if the exam says so) */
	public static StudentExamView from(Exam e, Attempt a, List<StudentQuestionView> questions) {
		return new StudentExamView(e.getId(), a.getId(), e.getTitle(), e.getInstructions(),
				e.getTimeLimitMinutes(), e.getMinExplanationWords(), a.getStartedAt(), List.copyOf(questions));
	}
}
